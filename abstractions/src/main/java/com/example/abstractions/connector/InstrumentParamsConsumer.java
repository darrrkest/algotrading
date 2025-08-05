package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;

public interface InstrumentParamsConsumer {
    SubscriptionResult subscribeParams(Instrument instrument);
    void unsubscribeParams(Instrument instrument);
}
