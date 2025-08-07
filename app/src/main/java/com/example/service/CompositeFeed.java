package com.example.service;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.ConnectorMessageVisitor;
import com.example.abstractions.connector.InstrumentParamsConsumer;
import com.example.abstractions.connector.OrderBookConsumer;

public interface CompositeFeed extends InstrumentParamsConsumer, OrderBookConsumer, ConnectorMessageVisitor {
    void addConnectorToComposite(Connector connector);
    void removeConnectorFromComposite(Connector connector);
}
