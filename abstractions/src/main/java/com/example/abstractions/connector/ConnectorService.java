package com.example.abstractions.connector;

import com.example.abstractions.connector.events.ConnectorMessageEvent;
import com.example.abstractions.connector.messages.ConnectorMessage;
import org.springframework.context.ApplicationEventPublisher;

public abstract class ConnectorService {
    private final ApplicationEventPublisher eventPublisher;

    protected ConnectorService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected void raiseMessageReceived(Object source, ConnectorMessage message) {
        eventPublisher.publishEvent(new ConnectorMessageEvent(source, message));
    }
}
