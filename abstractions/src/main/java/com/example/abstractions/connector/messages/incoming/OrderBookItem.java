package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Котировка из стакана
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
public final class OrderBookItem {

    @NotNull
    private OrderOperation operation;

    private double price;

    private long quantity;

    public OrderBookItem(@NotNull OrderOperation operation, double price, long quantity) {
        this.operation = operation;
        this.price = price;
        this.quantity = quantity;
    }
}
