package com.example.quik.adapter;

import com.example.quik.adapter.messages.QLMessage;

public interface QLAdapter {
    void sendMessage(QLMessage message);
    void start();
    void stop();
}
