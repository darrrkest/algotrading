package com.example.connector.messages.outgoing;

import com.example.connector.TransactionVisitor;
import com.example.execution.Order;
import com.example.execution.OrderExecutionCondition;
import com.example.execution.OrderOperation;
import com.example.execution.OrderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public final class NewOrderTransaction extends Transaction {
    String comment;
    OrderExecutionCondition executionCondition;
    OrderOperation operation;
    BigDecimal price;
    int size;
    OrderType type;
    LocalDateTime goodTill;

    @Override
    void Visit(TransactionVisitor visitor) {
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
