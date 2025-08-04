package com.example.connector.messages.incoming;

import com.example.execution.OrderOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderBookItem {

    public OrderBookItem(OrderOperation operation, BigDecimal price, long quantity) {
        this.operation = operation;
        this.price = price;
        this.quantity = quantity;
    }

    private OrderOperation operation;

    private BigDecimal price;

    private long quantity;
}
