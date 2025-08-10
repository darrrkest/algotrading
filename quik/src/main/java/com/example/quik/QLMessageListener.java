package com.example.quik;

import com.example.quik.adapter.messages.QLMessage;
import org.jetbrains.annotations.NotNull;

public interface QLMessageListener {
    void onMessageReceived(@NotNull QLMessage message);
}
