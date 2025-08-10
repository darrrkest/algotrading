package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Инйормация о позиции по заданному инструменту на заданном счете
 */
@Getter
@SuperBuilder
public class PositionMessage extends AccountMessage {
    private @NotNull ConnectorType connectorType;
    private @NotNull Instrument instrument;
    private int quantity;
    private @Nullable BigDecimal price;
}
