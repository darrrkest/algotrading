package com.example.abstractions.connector.events;

import com.example.abstractions.connector.ConnectionStatus;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * Spring-event с изменением статуса подключения коннектора {@link com.example.abstractions.connector.Connector}
 */
@Getter
public final class ConnectionStatusChangedEvent extends ApplicationEvent {

    /**
     * Статус
     */
    private @NotNull final ConnectionStatus status;

    public ConnectionStatusChangedEvent(Object source, @NotNull ConnectionStatus status) {
        super(source);
        this.status = status;
    }
}
