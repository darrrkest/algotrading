package com.example.abstractions.connector;

public interface Connector extends AdapterConnectionStatusChangeListener, AutoCloseable {
    ConnectorType getType();
    ConnectionStatus getConnectionStatus();
    Feed getFeed();
    void start();
    void stop();
}
