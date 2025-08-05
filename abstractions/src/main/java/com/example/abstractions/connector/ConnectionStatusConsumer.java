package com.example.abstractions.connector;

@FunctionalInterface
public interface ConnectionStatusConsumer {
    void handleConnectionStatus(ConnectionStatus status);
}
