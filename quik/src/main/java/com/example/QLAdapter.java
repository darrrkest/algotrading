package com.example;

import com.example.messages.QLMessage;

public interface QLAdapter {
    void sendMessage(QLMessage message);
    void start();
}
