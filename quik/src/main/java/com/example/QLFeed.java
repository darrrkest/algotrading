package com.example;

import com.example.connector.Feed;
import com.example.connector.SubscriptionResult;
import com.example.symbology.Instrument;
import com.example.symbology.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QLFeed implements Feed {

    private final Logger log = LoggerFactory.getLogger(QLFeed.class);

    private final InstrumentService instrumentService;
    private final QLAdapter adapter;

    public QLFeed(InstrumentService instrumentService, QLAdapter adapter) {
        this.instrumentService = instrumentService;
        this.adapter = adapter;
    }

    @Override
    public SubscriptionResult SubscribeParams(Instrument instrument) {
        return null;
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
