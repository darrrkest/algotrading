package com.example.quik;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.Feed;
import com.example.abstractions.connector.OrderRouter;
import com.example.abstractions.symbology.InstrumentService;
import com.example.quik.adapter.QLAdapter;
import com.example.quik.adapter.QLAdapterImpl;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

@Component
public class QLConnectorFactory {

    private final InstrumentService instrumentService;
    private final ApplicationEventPublisher publisher;

    public QLConnectorFactory(InstrumentService instrumentService,
                              ApplicationEventPublisher publisher) {
        this.instrumentService = instrumentService;
        this.publisher = publisher;
    }

    public Connector createConnector(String host, int port) {
        QLAdapter adapter = new QLAdapterImpl(publisher, host, port);
        Feed feed = new QLFeedImpl(instrumentService, adapter, publisher);
        OrderRouter router = new QLRouterImpl(instrumentService, adapter, publisher);
        return new QLConnectorImpl(adapter, feed, router, publisher);
    }
}
