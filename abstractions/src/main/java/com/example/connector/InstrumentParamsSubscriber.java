package com.example.connector;

import com.example.symbology.Instrument;

public interface InstrumentParamsSubscriber {
    SubscriptionResult subscribeParams(Instrument instrument);
    void unsubscribeParams(Instrument instrument);
}
