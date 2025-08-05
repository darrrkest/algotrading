package com.example.quik;

import com.example.quik.messages.QLMessage;

public interface QLAdapter {
    void sendMessage(QLMessage message);
    void start();
    void stop();
}
