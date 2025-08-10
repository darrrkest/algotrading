package com.example.service;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.Feed;
import com.example.abstractions.connector.SubscriptionResult;
import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.connector.messages.incoming.*;
import com.example.abstractions.symbology.Instrument;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class CompositeFeedImpl implements CompositeFeed {
    private static final Logger log = LoggerFactory.getLogger(CompositeFeedImpl.class);
    private final Object lock = new Object();
    private HashSet<Connector> connectors;

    @Override
    @NotNull
    public SubscriptionResult subscribeParams(@NotNull Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeParams(@NotNull Instrument instrument) {

    }

    @Override
    @NotNull
    public SubscriptionResult subscribeOrderBook(@NotNull Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeOrderBook(@NotNull Instrument instrument) {

    }

    @Override
    public void visit(@NotNull TransactionReply message) {

    }

    @Override
    public void visit(@NotNull FillMessage message) {

    }

    @Override
    public void visit(@NotNull PositionMessage message) {

    }

    @Override
    public void visit(@NotNull MoneyPosition message) {

    }

    @Override
    public void visit(@NotNull InstrumentParams message) {

    }

    @Override
    public void visit(@NotNull OrderBook message) {

    }

    @Override
    public void visit(@NotNull ConnectorMessage message) {

    }

    @Override
    public void addConnectorToComposite(Connector connector) {
        synchronized (lock) {
            if (connectors.contains(connector)) {
                log.warn("Connector already exists in CompositeFeed");
                return;
            }

            connectors.add(connector);
        }
    }

    @Override
    public void removeConnectorFromComposite(Connector connector) {
        synchronized (lock) {
            if (!connectors.contains(connector)) {
                log.warn("Tried to remove connector that does not exist in CompositeFeed ");
                return;
            }

            connectors.remove(connector);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
