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
public class OrderStateChangeMessage extends ConnectorMessage {
    private @Nullable String orderExchangeId;
    private @Nullable BigDecimal price;
    private int activeSize;
    private int filledSize;
    private int size;
    private @Nullable UUID transactionId;
    private @NotNull OrderState state;
    private @NotNull LocalDateTime changeTime;
}
