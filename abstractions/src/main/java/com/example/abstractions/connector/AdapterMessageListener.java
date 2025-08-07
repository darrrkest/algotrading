package com.example.abstractions.connector;

@FunctionalInterface
public interface AdapterMessageListener<T extends AdapterMessage> {
    void onMessageReceived(T message);
}

