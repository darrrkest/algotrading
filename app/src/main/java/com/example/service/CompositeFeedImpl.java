package com.example.service;

import com.example.abstractions.connector.CompositeFeed;
import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.SubscriptionResult;
import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.connector.messages.incoming.*;
import com.example.abstractions.symbology.Instrument;
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
    public SubscriptionResult subscribeParams(Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeParams(Instrument instrument) {

    }

    @Override
    public SubscriptionResult subscribeOrderBook(Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeOrderBook(Instrument instrument) {

    }

    @Override
    public void visit(TransactionReply message) {

    }

    @Override
    public void visit(FillMessage message) {

    }

    @Override
    public void visit(PositionMessage message) {

    }

    @Override
    public void visit(MoneyPosition message) {

    }

    @Override
    public void visit(InstrumentParams message) {

    }

    @Override
    public void visit(OrderBook message) {

    }

    @Override
    public void visit(ConnectorMessage message) {

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
}
