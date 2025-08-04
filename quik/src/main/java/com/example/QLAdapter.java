package com.example;

import com.example.connector.SubscriptionResult;
import com.example.messages.QLMessage;

public interface QLAdapter {
    void sendMessage(QLMessage message);
    void start();

    void subscribe(AdapterMessageConsumer consumer);
    void unsubscribe(AdapterMessageConsumer consumer);
}
