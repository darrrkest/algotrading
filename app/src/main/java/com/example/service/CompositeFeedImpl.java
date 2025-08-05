package com.example.service;

import com.example.abstractions.connector.CompositeFeed;
import com.example.abstractions.connector.SubscriptionResult;
import com.example.abstractions.symbology.Instrument;
import org.springframework.stereotype.Service;

@Service
public class CompositeFeedImpl implements CompositeFeed {
    @Override
    public SubscriptionResult subscribeParams(Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeParams(Instrument instrument) {

    }

    @Override
    public SubscriptionResult subscribeOrderBook(Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeOrderBook(Instrument instrument) {

    }
}
