package com.example.abstractions.connector.events;

import com.example.abstractions.connector.messages.ConnectorMessage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * Spring-event с сообщением типа {@link ConnectorMessage}
 */
@Getter
public final class ConnectorMessageEvent extends ApplicationEvent {

    /**
     * Сообщение
     */
    private @NotNull final ConnectorMessage message;

    public ConnectorMessageEvent(Object source, @NotNull ConnectorMessage message) {
        super(source);
        this.message = message;
    }
}
