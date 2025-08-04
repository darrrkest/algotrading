package com.example.connector;

public interface Feed extends InstrumentParamsSubscriber, OrderBookSubscriber {
    void addMessageListener(ConnectorMessageConsumer listener);

    void removeMessageListener(ConnectorMessageConsumer listener);
}
