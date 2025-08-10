package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Котировка из стакана
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderBookItem {

    private @NotNull OrderOperation operation;

    private @NotNull BigDecimal price;

    private long quantity;

    public OrderBookItem(@NotNull OrderOperation operation,@NotNull BigDecimal price, long quantity) {
        this.operation = operation;
        this.price = price;
        this.quantity = quantity;
    }
}
