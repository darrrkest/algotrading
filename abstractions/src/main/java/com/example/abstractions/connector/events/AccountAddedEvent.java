package com.example.abstractions.connector.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AccountAddedEvent extends ApplicationEvent {

    private final String account;

    public AccountAddedEvent(Object source, String account) {
        super(source);
        this.account = account;
    }
}
