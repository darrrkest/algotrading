package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.execution.Order;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Транзакция на снятие заявки
 */
@Getter
@SuperBuilder
public final class KillOrderTransaction extends Transaction {

    // region required

    /**
     * Идентификатор снимаемой заявки
     */
    @NotNull
    private final String orderExchangeId;

    // endregion

    public static KillOrderTransaction fromOrder(Order order) {
        return builder()
                .account(order.getAccount())
                .instrument(order.getInstrument())
                .orderExchangeId(order.getOrderExchangeId())
                .transactionId(UUID.randomUUID())
                .build();
    }

    @Override
    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }
}
