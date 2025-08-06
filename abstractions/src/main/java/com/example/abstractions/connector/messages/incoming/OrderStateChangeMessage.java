package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.execution.OrderState;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public class OrderStateChangeMessage extends ConnectorMessage {
    private String orderExchangeId;
    private BigDecimal price;
    private int activeSize;
    private int filledSize;
    private int size;
    private UUID transactionId;
    private OrderState state;
    private LocalDateTime changeTime;
}
