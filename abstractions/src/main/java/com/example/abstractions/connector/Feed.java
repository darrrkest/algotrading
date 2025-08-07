package com.example.abstractions.connector;

public interface Feed<L extends AdapterMessage> extends AdapterMessageListener<L>, InstrumentParamsConsumer, OrderBookConsumer, AutoCloseable {
}
