package com.example.connector;

import com.example.symbology.Instrument;

public interface OrderBookSubscriber {
    SubscriptionResult SubscribeOrderBook(Instrument instrument);
    void UnsubscribeOrderBook(Instrument instrument);
}
