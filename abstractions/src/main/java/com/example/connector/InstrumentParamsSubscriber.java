package com.example.connector;

import com.example.symbology.Instrument;

public interface InstrumentParamsSubscriber {
    SubscriptionResult SubscribeParams(Instrument instrument);
    void UnsubscribeParams(Instrument instrument);
}
