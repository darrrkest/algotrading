package com.example.quik;

import com.example.abstractions.connector.ConnectionStatus;
import com.example.abstractions.connector.ConnectionStatusChangedEventArgs;
import com.example.quik.messages.QLEnvelope;
import com.example.quik.messages.QLEnvelopeAcknowledgment;
import com.example.quik.messages.QLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public final class QLAdapterImpl implements QLAdapter {
    private static final int RECEIVE_TIMEOUT_MS = 10;
    private static final int SEND_TIMEOUT_MS = 10;

    private static final Logger log = LoggerFactory.getLogger(QLAdapterImpl.class);
    private final Object lock = new Object();
    private final int port;
    private final InetAddress ip;
    private Socket socket;
    private ConnectionStatus connectionStatus = ConnectionStatus.UNDEFINED;

    private final BlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<QLMessage> outgoingMessages = new LinkedBlockingQueue<>();
    private BufferedReader reader;
    private BufferedWriter writer;

    private final ApplicationEventPublisher eventPublisher;
    private ExecutorService executor;

    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final Semaphore connectedSemaphore = new Semaphore(0);

    public QLAdapterImpl(ApplicationEventPublisher eventPublisher, String ip, int port) {
        this.eventPublisher = eventPublisher;
        this.ip = resolveIp(ip);
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
        cancelled.set(false);
        synchronized (lock) {
            if (ConnectionStatus.isActive(connectionStatus)) return;
            connectionStatus = ConnectionStatus.CONNECTING;
        }

        try {
            executor = Executors.newFixedThreadPool(4);
            createSocket();
            executor.submit(this::runReceive);
            executor.submit(this::runSend);
            executor.submit(this::runDeserialize);

            executor.submit(this::runConnect);
            onConnectionStatusChanged();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        synchronized (lock) {
            if (!ConnectionStatus.isActive(connectionStatus)) return;
        }

        try {
            cancelled.set(true);
            connectedSemaphore.drainPermits();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();

            socket = null;
            reader = null;
            writer = null;
            executor.shutdown();
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (Exception e) {
            log.error("Got error while stopping QLAdapter: {}", e.getMessage());
        }

        synchronized (lock) {
            connectionStatus = ConnectionStatus.DISCONNECTED;
        }

        connected.set(false);
        onConnectionStatusChanged();
    }

    private void runConnect() {
        Thread.currentThread().setName("QL_CONNECT");

        while (!cancelled.get()) {
            try {
                sleep(1000);
                createSocket();

                if (socket == null) {
                    log.warn("Socket is null");
                    recreateSocket();
                    continue;
                }

                if (!connected.get()) {
                    connectionStatus = ConnectionStatus.DISCONNECTED;
                    recreateSocket();
                    onConnectionStatusChanged();
                } else {
                    continue;
                }

                log.info("Connecting to QUIK...");
                socket.connect(new InetSocketAddress(ip, port));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "windows-1251"));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "windows-1251"));
                log.info("Connected to QUIK");

                connected.set(true);
                connectionStatus = ConnectionStatus.CONNECTED;
                onConnectionStatusChanged();

                // Освобожаем два места под два потока. release(2) может быть превышение
                while (connectedSemaphore.availablePermits() < 1) {
                    connectedSemaphore.release();
                }

            } catch (SocketTimeoutException e) {
                log.info("Socket timed out while connecting to QUIK: {}", e.getMessage());
                recreateSocket();
            } catch (Exception e) {
                log.error("Connection error: {}", e.getMessage(), e);
                recreateSocket();
            }
        }
    }

    private void runReceive() {
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

    private void runSend() {
        Thread.currentThread().setName("QL_SEND");

        long lastHeartbeat = System.currentTimeMillis();

        while (!cancelled.get()) {
            try {
                if (!connected.get()) {
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

            } catch (SocketException e) {
                log.error("Connection lost: {}", e.getMessage(), e);
                connected.set(false);
                connectionStatus = ConnectionStatus.DISCONNECTED;
                onConnectionStatusChanged();
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
        eventPublisher.publishEvent(new QLMessageEventArgs(this, message));
    }

    private void onConnectionStatusChanged() {
        log.info("QLAdapterImpl status changed to: {}", connectionStatus);
        eventPublisher.publishEvent(new ConnectionStatusChangedEventArgs(this, connectionStatus));
    }

    private boolean ensureConnected() {
        if (connected.get()) {
            return true;
        }

        connected.set(false);

        try {
            connectedSemaphore.acquire();
        } catch (InterruptedException e) {
            return false;
        }

        return true;
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

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
