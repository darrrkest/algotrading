package com.example.connector.messages.outgoing;

import com.example.connector.TransactionVisitor;
import com.example.execution.Order;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
public class KillOrderTransaction extends Transaction {

    String orderExchangeId;

    @Override
    void Visit(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    public static KillOrderTransaction fromOrder(Order order) {
        return builder()
                .account(order.getAccount())
                .instrument(order.getInstrument())
                .orderExchangeId(order.getOrderExchangeId())
                .transactionId(UUID.randomUUID())
                .build();
    }
}
