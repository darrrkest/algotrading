package com.example.quik;

import com.example.abstractions.connector.ConnectionStatus;
import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.connector.Feed;
import com.example.abstractions.symbology.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QLConnectorImpl implements QLConnector {
    public static int DEFAULT_PORT = 1250;

    private final Logger log = LoggerFactory.getLogger(QLConnectorImpl.class);

    private final QLAdapter adapter;
    private final QLFeed feed;
    private final QLRouter router;

    private ConnectionStatus connectionStatus = ConnectionStatus.UNDEFINED;

    public QLConnectorImpl(InstrumentService instrumentService) {
        this.adapter = new QLAdapterImpl("localhost", DEFAULT_PORT);

        this.feed = new QLFeedImpl(instrumentService, adapter);
        this.router = new QLRouter();
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
}
