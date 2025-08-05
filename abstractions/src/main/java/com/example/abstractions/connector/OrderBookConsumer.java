package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;

public interface OrderBookConsumer {
    SubscriptionResult subscribeOrderBook(Instrument instrument);
    void unsubscribeOrderBook(Instrument instrument);
}
