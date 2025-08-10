package com.example.abstractions.connector;

import com.example.abstractions.connector.events.ConnectorMessageEvent;
import com.example.abstractions.connector.messages.ConnectorMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Базовый класс сервиса коннектора.
 * <p>
 * Выплевывает сообщения наружу в виде spring-event'ов типа {@link ConnectorMessageEvent}
 */
public abstract class ConnectorService {
    protected final ApplicationEventPublisher eventPublisher;

    protected ConnectorService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Опубликовать сообщение типа {@link ConnectorMessageEvent}
     */
    protected void raiseMessageReceived(Object source, @NotNull ConnectorMessage message) {
        eventPublisher.publishEvent(new ConnectorMessageEvent(source, message));
    }
}
