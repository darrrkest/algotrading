package com.example.abstractions.connector;

@FunctionalInterface
public interface AdapterConnectionStatusChangeListener {
    void onConnectionStatusChange(ConnectionStatus status);
}
