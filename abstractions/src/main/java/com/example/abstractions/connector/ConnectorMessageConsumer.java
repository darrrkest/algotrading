package com.example.abstractions.connector;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConnectorMessageConsumer extends Consumer<ConnectorMessageEventArgs> {
}
