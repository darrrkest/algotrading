package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс собственной(!) сделки
 */
@Getter
@SuperBuilder
public class FillMessage extends AccountMessage {
    private @NotNull Instrument instrument;

    private @NotNull LocalDateTime dateTime;

    private @NotNull OrderOperation operation;

    private @NotNull BigDecimal price;
    private int size;

    /**
     * Биржевой номер сделки
     */
    private @NotNull String exchangeId;

    /**
     * Биржевой номер заявки
     */
    private @NotNull String exchangeOrderId;

    private @NotNull LiquidityIndicator liquidityIndicator;

    private @Nullable BigDecimal commission;
}
