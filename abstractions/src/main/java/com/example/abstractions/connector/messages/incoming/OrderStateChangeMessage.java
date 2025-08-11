package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.execution.OrderState;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public final class OrderStateChangeMessage extends ConnectorMessage {

    @Nullable
    private final String orderExchangeId;

    private final double price;

    private final int activeSize;

    private final int filledSize;

    private final int size;

    @Nullable
    private final UUID transactionId;

    @NotNull
    private final OrderState state;

    @NotNull
    private final LocalDateTime changeTime;
}
