package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;

public interface Feed extends AutoCloseable {
    SubscriptionResult subscribeParams(Instrument instrument);
    void unsubscribeParams(Instrument instrument);

    SubscriptionResult subscribeOrderBook(Instrument instrument);
    void unsubscribeOrderBook(Instrument instrument);
}
