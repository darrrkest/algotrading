package com.example.connector;

import com.example.symbology.Instrument;

public interface OrderBookSubscriber {
    SubscriptionResult subscribeOrderBook(Instrument instrument);
    void unsubscribeOrderBook(Instrument instrument);
}
