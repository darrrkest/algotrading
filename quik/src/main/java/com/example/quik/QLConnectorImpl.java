package com.example.quik;

import com.example.abstractions.connector.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class QLConnectorImpl implements Connector {
    public static int DEFAULT_PORT = 1250;

    private final Logger log = LoggerFactory.getLogger(QLConnectorImpl.class);

    private final QLAdapter adapter;
    private final ApplicationEventPublisher eventPublisher;
    private final QLFeed feed;
    private final QLRouter router;

    private ConnectionStatus connectionStatus = ConnectionStatus.UNDEFINED;

    public QLConnectorImpl(QLFactory qlFactory) {
        this.adapter = qlFactory.createAdapter("localhost", DEFAULT_PORT);
        this.feed = qlFactory.createFeed();
        this.router = qlFactory.createRouter();

        this.eventPublisher = qlFactory.getEventPublisher();
    }

    @Override
    public ConnectorType getType() {
        return ConnectorType.QUIK;
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    @Override
    public Feed getFeed() {
        return feed;
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

    @EventListener
    public void onConnectionStatusChanged(ConnectionStatusChangedEventArgs event) {
        if (event.source() != this.adapter) {
            return;
        }
        log.info("Connection status of adapter {} changed {} → {}", this, connectionStatus, event.status());

        boolean changed = connectionStatus != event.status();
        connectionStatus = event.status();

        if (changed) {
            eventPublisher.publishEvent(new ConnectionStatusChangedEventArgs(this, connectionStatus));
        }
    }
}
