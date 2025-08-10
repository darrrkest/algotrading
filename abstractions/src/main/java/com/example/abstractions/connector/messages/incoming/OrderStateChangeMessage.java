package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.execution.OrderState;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public final class OrderStateChangeMessage extends ConnectorMessage {

    @Nullable
    private String orderExchangeId;

    @Nullable
    private BigDecimal price;
    private int activeSize;
    private int filledSize;
    private int size;

    @Nullable
    private UUID transactionId;

    @NotNull
    private OrderState state;

    @NotNull
    private LocalDateTime changeTime;
}
