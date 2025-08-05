package com.example.ib.adapter;

import com.example.abstractions.connector.Feed;
import com.example.abstractions.connector.SubscriptionResult;
import com.example.abstractions.symbology.Instrument;

public class IBFeed implements Feed {

    @Override
    public SubscriptionResult subscribeParams(Instrument instrument) {
        return null;
    }

    @Override
    public void unsubscribeParams(Instrument instrument) {

    }

    @Override
    public SubscriptionResult subscribeOrderBook(Instrument instrument) {
        return null;
    }

    @Override
    public void unsubscribeOrderBook(Instrument instrument) {

    }
}
