package com.example.quik;

import com.example.abstractions.connector.ConnectionStatusConsumer;
import com.example.quik.messages.QLMessage;

public interface QLAdapterMessageConsumer extends ConnectionStatusConsumer {
    void handleQLMessage(QLMessage message);
}
