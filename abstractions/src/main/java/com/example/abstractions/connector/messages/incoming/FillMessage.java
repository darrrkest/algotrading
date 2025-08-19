package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * Класс собственной(!) сделки
 */
@Getter
@SuperBuilder
public class FillMessage extends AccountMessage {
    @NotNull
    private Instrument instrument;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private OrderOperation operation;

    private double price;
    private int size;

    /**
     * Биржевой номер сделки
     */
    @NotNull
    private String exchangeId;

    /**
     * Биржевой номер заявки
     */
    @NotNull
    private String exchangeOrderId;

    @NotNull
    private LiquidityIndicator liquidityIndicator;

    private double commission;
}
