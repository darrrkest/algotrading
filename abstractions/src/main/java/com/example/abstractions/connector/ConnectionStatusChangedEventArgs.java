package com.example.abstractions.connector;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConnectionStatusChangedEventArgs extends ApplicationEvent {

    ConnectionStatus status;

    public ConnectionStatusChangedEventArgs(Object source, ConnectionStatus status) {
        super(source);
        this.status = status;
    }
}
