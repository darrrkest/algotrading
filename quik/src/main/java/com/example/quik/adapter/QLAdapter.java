package com.example.quik.adapter;

import com.example.abstractions.connector.AdapterConnectionStatusChangeListener;
import com.example.quik.QLMessageListener;
import com.example.quik.adapter.messages.QLMessage;
import org.jetbrains.annotations.NotNull;

public interface QLAdapter extends AutoCloseable {
    void sendMessage(@NotNull QLMessage message);

    void start();
    void stop();

    void addMessageListener(@NotNull QLMessageListener listener);
    void removeMessageListener(@NotNull QLMessageListener listener);

    void addConnectionStatusListener(@NotNull AdapterConnectionStatusChangeListener listener);
    void removeConnectionStatusListener(@NotNull AdapterConnectionStatusChangeListener listener);
}
