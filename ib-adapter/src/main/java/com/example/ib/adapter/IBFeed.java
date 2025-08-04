package com.example.ib.adapter;

import com.example.connector.ConnectorMessageConsumer;
import com.example.connector.Feed;
import com.example.connector.SubscriptionResult;
import com.example.symbology.Instrument;

public class IBFeed implements Feed {
    @Override
    public void addMessageListener(ConnectorMessageConsumer listener) {

    }

    @Override
    public void removeMessageListener(ConnectorMessageConsumer listener) {

    }

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
