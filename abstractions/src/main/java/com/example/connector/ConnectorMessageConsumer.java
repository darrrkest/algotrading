package com.example.connector;

import com.example.ConnectorMessageEventArgs;

@FunctionalInterface
public interface ConnectorMessageConsumer {
    void handle(ConnectorMessageEventArgs event);
}
