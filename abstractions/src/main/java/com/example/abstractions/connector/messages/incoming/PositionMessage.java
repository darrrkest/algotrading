package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Информация о позиции по заданному инструменту на заданном счете
 */
@Getter
@SuperBuilder
public final class PositionMessage extends AccountMessage {
    @NotNull
    private final ConnectorType connectorType;

    @NotNull
    private final Instrument instrument;

    private final int quantity;

    private final double price;
}
