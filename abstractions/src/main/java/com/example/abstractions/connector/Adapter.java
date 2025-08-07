package com.example.abstractions.connector;

public interface Adapter<M extends AdapterMessage, L extends AdapterMessageListener<M>> extends AutoCloseable {
    void sendMessage(M message);

    void start();
    void stop();

    void addMessageListener(L listener);
    void removeMessageListener(L listener);

    void addConnectionStatusListener(AdapterConnectionStatusChangeListener listener);
    void removeConnectionStatusListener(AdapterConnectionStatusChangeListener listener);
}
