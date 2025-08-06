package com.example.quik;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.symbology.InstrumentService;
import lombok.Getter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class QLFactory {

    @Getter
    private final ApplicationEventPublisher eventPublisher;
    private final InstrumentService instrumentService;

    private Connector qlConnector;
    private QLRouter qlRouter;
    private QLFeed qlFeed;
    private QLAdapter qlAdapter;

    public QLFactory(ApplicationEventPublisher eventPublisher, InstrumentService instrumentService) {
        this.eventPublisher = eventPublisher;
        this.instrumentService = instrumentService;
    }

    public Connector createConnector() {
        qlConnector = new QLConnectorImpl(this);
        return qlConnector;
    }

    public QLRouter createRouter() {
        //qlRouter = new QLRouterImpl(instrumentService, qlAdapter);
        return qlRouter;
    }

    public QLFeed createFeed() {
        qlFeed = new QLFeedImpl(instrumentService, qlAdapter, eventPublisher);
        return qlFeed;
    }

    public QLAdapter createAdapter(String ip, int port) {
        qlAdapter = new QLAdapterImpl(eventPublisher, ip, port);
        return qlAdapter;
    }
}
