package com.example.abstractions.connector.events;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * Spring-event с добавлением счета
 */
@Getter
public final class AccountAddedEvent extends ApplicationEvent {

    /**
     * Счет
     */
    @NotNull
    private final String account;

    public AccountAddedEvent(Object source, @NotNull String account) {
        super(source);
        this.account = account;
    }
}
