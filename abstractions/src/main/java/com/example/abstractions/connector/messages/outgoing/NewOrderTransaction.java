package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.execution.Order;
import com.example.abstractions.execution.OrderExecutionCondition;
import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.execution.OrderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Транзакция на постановку новой заявки
 */
@Getter
@SuperBuilder
public final class NewOrderTransaction extends Transaction {
    @NotNull
    private String comment;

    @NotNull
    private OrderExecutionCondition executionCondition;

    @NotNull
    private OrderOperation operation;

    @NotNull
    private BigDecimal price;

    private int size;

    @NotNull
    private OrderType type;

    @NotNull
    private LocalDateTime goodTill;

    @Override
    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }

    public static NewOrderTransaction fromOrder(Order order) {
        return builder()
                .account(order.getAccount())
                .instrument(order.getInstrument())
                .operation(order.getOperation())
                .size(order.getSize())
                .price(order.getPrice())
                .comment(order.getComment())
                .goodTill(order.getGoodTill())
                .transactionId(UUID.randomUUID())
                .type(OrderType.LIMIT)
                .executionCondition(OrderExecutionCondition.PUT_UN_QUEUE)
                .build();
    }
}
