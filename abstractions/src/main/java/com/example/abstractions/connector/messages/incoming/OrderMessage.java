package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.execution.Order;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Сообщение о внешней (выставленной не через адаптер) заявке
 */
@Getter
@SuperBuilder
public class OrderMessage extends ConnectorMessage {
    /**
     * Заявка
     */
    public @NotNull Order order;
}
