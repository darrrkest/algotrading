package com.example.service;

import com.example.connector.CompositeFeed;
import com.example.connector.ConnectorMessageConsumer;
import com.example.connector.SubscriptionResult;
import com.example.symbology.Instrument;
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

    @Override
    public void addMessageListener(ConnectorMessageConsumer listener) {

    }

    @Override
    public void removeMessageListener(ConnectorMessageConsumer listener) {

    }
}
