package com.example.ib.adapter;

import com.example.abstractions.connector.Feed;
import com.example.abstractions.connector.SubscriptionResult;
import com.example.abstractions.symbology.Instrument;
import org.jetbrains.annotations.NotNull;

public class IBFeed implements Feed {

    @Override
    @NotNull
    public SubscriptionResult subscribeParams(@NotNull Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeParams(@NotNull Instrument instrument) {

    }

    @Override
    @NotNull
    public SubscriptionResult subscribeOrderBook(@NotNull Instrument instrument) {
        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeOrderBook(@NotNull Instrument instrument) {

    }

    @Override
    public void close() throws Exception {

    }
}
