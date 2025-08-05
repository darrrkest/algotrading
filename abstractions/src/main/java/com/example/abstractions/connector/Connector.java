package com.example.abstractions.connector;

public interface Connector extends FeedMessageConsumer {
    ConnectorType getType();
    ConnectionStatus getConnectionStatus();
    Feed getFeed();
    void start();
    void stop();
}
