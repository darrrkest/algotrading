package com.example.abstractions.connector.events;

import com.example.abstractions.connector.ConnectionStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConnectionStatusChangedEvent extends ApplicationEvent {

    private final ConnectionStatus status;

    public ConnectionStatusChangedEvent(Object source, ConnectionStatus status) {
        super(source);
        this.status = status;
    }
}
