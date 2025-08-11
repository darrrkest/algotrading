package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.execution.StopOrder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Сообщение о внешней (выставленной не через адаптер) заявке
 */
@Getter
@SuperBuilder
public final class StopOrderMessage extends ConnectorMessage {
    /**
     * Заявка
     */
    @NotNull
    public final StopOrder stopOrder;
}