package com.example.service;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.messages.ConnectorMessageVisitor;
import com.example.abstractions.connector.Feed;

public interface CompositeFeed extends Feed, ConnectorMessageVisitor {
    void addConnectorToComposite(Connector connector);
    void removeConnectorFromComposite(Connector connector);
}
