package com.example;

import com.example.messages.QLMessage;

public interface AdapterMessageConsumer {
    void HandleAdapterMessage(QLMessage message);
}
