package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;

public record SubscriptionResult(Instrument Instrument, boolean Success, String Message) {
    public static SubscriptionResult OK(Instrument instrument)
    {
        return new SubscriptionResult(instrument, true, "");
    }

    public static SubscriptionResult Error(Instrument instrument, String message)
    {
        return new SubscriptionResult(instrument, false, message);
    }
}

