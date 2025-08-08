package com.example.quik.adapter;

import com.example.abstractions.connector.AdapterConnectionStatusChangeListener;
import com.example.quik.QLMessageListener;
import com.example.quik.adapter.messages.QLMessage;

public interface QLAdapter extends AutoCloseable {
    void sendMessage(QLMessage message);

    void start();
    void stop();

    void addMessageListener(QLMessageListener listener);
    void removeMessageListener(QLMessageListener listener);

    void addConnectionStatusListener(AdapterConnectionStatusChangeListener listener);
    void removeConnectionStatusListener(AdapterConnectionStatusChangeListener listener);
}
