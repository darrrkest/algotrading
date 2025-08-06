package com.example.abstractions.connector;

public interface CompositeFeed extends Feed, ConnectorMessageVisitor {
    void addConnectorToComposite(Connector connector);
    void removeConnectorFromComposite(Connector connector);
}
