package com.example.quik;

import com.example.abstractions.connector.ConnectionStatus;
import com.example.quik.messages.QLEnvelope;
import com.example.quik.messages.QLEnvelopeAcknowledgment;
import com.example.quik.messages.QLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.lang.Thread.sleep;

public final class QLAdapterImpl implements QLAdapter {
    private static final int RECEIVE_TIMEOUT_MS = 10;
    private static final int SEND_TIMEOUT_MS = 10;

    private final Logger log = LoggerFactory.getLogger(QLAdapterImpl.class);
    private final Object lock = new Object();
    private final int port;
    private final InetAddress ip;
    private Socket socket;
    private ConnectionStatus connectionStatus = ConnectionStatus.UNDEFINED;

    private final BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<QLMessage> outgoingMessages = new LinkedBlockingQueue<>();
    private BufferedReader reader;
    private BufferedWriter writer;

    private ExecutorService executor;

    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public QLAdapterImpl(String ipAddress, int port) {
        this.ip = resolveIp(ipAddress);
        this.port = port == 0 ? QLConnectorImpl.DEFAULT_PORT : port;
    }

    private InetAddress resolveIp(String host) {
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            try {
                return InetAddress.getByName("127.0.0.1");
            } catch (UnknownHostException ignored) {
            }
        }

        return null;
    }

    @Override
    public void sendMessage(QLMessage message) {
        outgoingMessages.add(message);
    }

    @Override
    public void start() {
        synchronized (lock) {
            if (ConnectionStatus.isActive(connectionStatus)) return;

            connectionStatus = ConnectionStatus.CONNECTING;
        }

        try {
            executor = Executors.newFixedThreadPool(4);
            createSocket();
            executor.submit(() -> {
                try {
                    runConnect();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
            executor.submit(() -> {
                try {
                    runReceive();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
            executor.submit(() -> {
                try {
                    runSend();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
            executor.submit(this::runDeserialize);
            onStatusChanged();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        synchronized (lock) {
            if (ConnectionStatus.isActive(connectionStatus)) return;
        }

        try {
            cancelled.set(true);
            socket.close();
            socket = null;
            reader = null;
            writer = null;
            executor.shutdown();
        } catch (Exception e) {
            log.error("Got error while stopping QLAdapter: {}", e.getMessage());
        }

        synchronized (lock) {
            connectionStatus = ConnectionStatus.DISCONNECTED;
        }

        onStatusChanged();
    }

    private final List<QLAdapterMessageConsumer> subs = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(QLAdapterMessageConsumer consumer) {
        subs.add(consumer);
    }

    @Override
    public void unsubscribe(QLAdapterMessageConsumer consumer) {
        subs.remove(consumer);
    }

    private void runConnect() {
        Thread.currentThread().setName("QL_CONNECT");

        while (!cancelled.get()) {
            try {
                createSocket();

                if (socket == null) {
                    log.warn("Socket is null");
                    recreateSocket();
                    continue;
                }

                if (socket.isConnected()) {
                    log.warn("Socket connected for some reason. Recreating socket");
                    connectionStatus = ConnectionStatus.DISCONNECTING;
                    recreateSocket();
                }

                log.info("Connecting to QUIK...");
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 3000);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "windows-1251"));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "windows-1251"));
                log.info("Connected to QUIK");

                connectionStatus = ConnectionStatus.CONNECTED;
                onStatusChanged();

            } catch (SocketTimeoutException e) {
                log.info("Socket timed out while connecting to QUIK: {}", e.getMessage());
                recreateSocket();
            } catch (Exception e) {
                log.error("Connection error: {}", e.getMessage(), e);
                recreateSocket();
            }
        }
    }

    private void runReceive() throws InterruptedException {
        Thread.currentThread().setName("QL_RECEIVE");

        while (!cancelled.get()) {
            try {
                if (!ensureConnected()) {
                    sleep(RECEIVE_TIMEOUT_MS);
                    continue;
                }

                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    incomingMessages.offer(line);
                    //log.trace("CHUNK: {}", line);
                }

            } catch (IOException e) {
                log.error("Receive error: {}", e.getMessage(), e);
                sleep(1000);
            }
        }
    }

    private void runSend() throws InterruptedException {
        Thread.currentThread().setName("QL_SEND");

        long lastHeartbeat = System.currentTimeMillis();

        while (!cancelled.get()) {
            try {
                if (!ensureConnected()) {
                    sleep(SEND_TIMEOUT_MS);
                    continue;
                }

                QLMessage message = outgoingMessages.poll(100, TimeUnit.MILLISECONDS);
                if (message != null) {
                    String json = QLMapper.serialize(message);
                    writer.write(json);
                    writer.newLine();
                    writer.flush();
                } else if (System.currentTimeMillis() - lastHeartbeat > 2000) {
                    writer.write(QLMapper.heartbeatJson());
                    writer.newLine();
                    writer.flush();
                    lastHeartbeat = System.currentTimeMillis();
                }

            } catch (Exception e) {
                log.error("Send error: {}", e.getMessage(), e);
                sleep(1000);
            }
        }
    }

    private void runDeserialize() {
        Thread.currentThread().setName("QL_DESERIALIZE");

        while (!cancelled.get()) {
            QLEnvelope envelope = null;
            try {
                String json = incomingMessages.poll(RECEIVE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (json == null) continue;

                envelope = QLMapper.deserializeEnvelope(json);
                if (envelope.getCount() <= 0) continue;
                for (QLMessage message : envelope.getBody()) {
                    onMessageReceived(message);
                }

                outgoingMessages.offer(new QLEnvelopeAcknowledgment(envelope.getId()));

            } catch (Exception e) {
                log.error("Error while QL messages parsing: {}, JSON {}", e.getMessage(), envelope);
            }
        }
    }

    private void onMessageReceived(QLMessage message) {
        log.info(message.toString());
        for (QLAdapterMessageConsumer consumer : subs) {
            consumer.handleQLMessage(message);
        }
    }

    private void onStatusChanged() {
        //log.info("QLAdapterImpl status changed to: {}", connectionStatus);
        //for (QLAdapterMessageConsumer consumer : subs) {
        //    consumer.accept(connectionStatus);
        //}
    }

    private boolean ensureConnected() {
        //if (socket != null && socket.isConnected()) return true;
        return socket != null && socket.isConnected();
    }

    private void createSocket() {
        if (socket != null) return;

        try {
            socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setReceiveBufferSize(1_000_000);
        } catch (SocketException e) {
            //error creating socket
        }

    }

    private void recreateSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Got error while closing socket {}", e.getMessage());
        }

        socket = null;
        createSocket();
    }

    //region subs

    private final List<Consumer<ConnectionStatus>> connectionStatusListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<QLMessage>> messageListeners = new CopyOnWriteArrayList<>();

    public void addConnectionStatusListener(Consumer<ConnectionStatus> listener) {
        connectionStatusListeners.add(listener);
    }

    public void addMessageListener(Consumer<QLMessage> listener) {
        messageListeners.add(listener);
    }

    private void notifyConnectionStatus(ConnectionStatus status) {
        for (var listener : connectionStatusListeners) {
            listener.accept(status);
        }
    }

    private void notifyMessage(QLMessage message) {
        for (var listener : messageListeners) {
            listener.accept(message);
        }
    }

    //endregion
}
