package com.example.service;

import com.example.connector.CompositeFeed;
import com.example.connector.SubscriptionResult;
import com.example.symbology.Instrument;
import org.springframework.stereotype.Service;

@Service
public class CompositeFeedImpl implements CompositeFeed {
    @Override
    public SubscriptionResult SubscribeParams(Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void UnsubscribeParams(Instrument instrument) {

    }

    @Override
    public SubscriptionResult SubscribeOrderBook(Instrument instrument) {
        return null;
    }

    @Override
    public void UnsubscribeOrderBook(Instrument instrument) {

    }
}
