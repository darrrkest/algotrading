package com.example;

import com.example.symbology.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QLConnector {
    public static int DEFAULT_PORT = 1250;

    private final Logger log = LoggerFactory.getLogger(QLConnector.class);

    private final QLAdapter adapter;
    private final QLFeed feed;
    private final QLRouter router;

    public QLConnector(InstrumentService instrumentService) {
        this.adapter = new QLAdapterImpl("localhost", DEFAULT_PORT);
        this.feed = new QLFeedImpl(instrumentService, adapter);
        this.router = new QLRouter();
    }
}
