package com.example.abstractions.connector.events;

import com.example.abstractions.connector.messages.ConnectorMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConnectorMessageEvent extends ApplicationEvent {

    private final ConnectorMessage message;

    public ConnectorMessageEvent(Object source, ConnectorMessage message) {
        super(source);
        this.message = message;
    }
}
