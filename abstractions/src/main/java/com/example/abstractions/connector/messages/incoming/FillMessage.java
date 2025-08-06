package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class FillMessage extends AccountMessage {
    private Instrument instrument;

    private LocalDateTime dateTime;

    private OrderOperation operation;

    private BigDecimal price;
    private int size;

    /**
     * Биржевой номер заявки
     */
    private String exchangeId;

    private LiquidityIndicator liquidityIndicator;

    private BigDecimal commission;

}
