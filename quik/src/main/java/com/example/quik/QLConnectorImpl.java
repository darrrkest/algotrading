package com.example.quik;

import com.example.abstractions.connector.*;
import com.example.abstractions.connector.events.ConnectionStatusChangedEvent;
import com.example.quik.adapter.QLAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public final class QLConnectorImpl implements Connector {
    public static int DEFAULT_PORT = 1250;

    private static final Logger log = LoggerFactory.getLogger(QLConnectorImpl.class);

    private final ApplicationEventPublisher eventPublisher;
    private final QLAdapter adapter;
    private final QLFeed feed;
    private final QLRouter router;

    private ConnectionStatus connectionStatus = ConnectionStatus.UNDEFINED;

    public QLConnectorImpl(QLAdapter adapter, QLFeed feed, QLRouter router, ApplicationEventPublisher eventPublisher) {
        this.adapter = adapter;
        this.feed = feed;
        this.router = router;
        this.eventPublisher = eventPublisher;

        adapter.addConnectionStatusListener(this);
    }

    @Override
    @NotNull
    public ConnectorType getType() {
        return ConnectorType.QUIK;
    }

    @Override
    @NotNull
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    @NotNull
    public Feed getFeed() {
        return feed;
    }

    @Override
    @NotNull
    public OrderRouter getRouter() {
        return router;
    }

    @Override
    public void start() {
        log.info("Starting QL connector");
        adapter.start();
        connectionStatus = ConnectionStatus.CONNECTING;
    }

    @Override
    public void stop() {
        log.info("Stopping QL connector");
        connectionStatus = ConnectionStatus.DISCONNECTING;

        try {
            adapter.stop();
        }
        catch (Exception e) {
            log.error("Error stopping QL adapter", e);
        }
    }

    @Override
    public void onConnectionStatusChange(@NotNull ConnectionStatus status) {
        log.info("Connection status of adapter {} changed {} → {}", this, connectionStatus, status);

        boolean changed = connectionStatus != status;
        connectionStatus = status;

        if (changed) {
            eventPublisher.publishEvent(new ConnectionStatusChangedEvent(this, connectionStatus));
        }
    }

    @Override
    public void close() {
        adapter.removeConnectionStatusListener(this);
    }
}
