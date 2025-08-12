package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.execution.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Транзакция на постановку новой заявки
 */
@Getter
@SuperBuilder
public final class NewOrderTransaction extends Transaction {

    // region required

    /**
     * Цена заявки
     */
    private final double price;

    /**
     * Количество контрактов/лотов
     */
    private final int size;

    /**
     * Операция заявки
     */
    @NotNull
    private final OrderOperation operation;

    /**
     * Условие исполнения заявки
     */
    @NotNull
    private final OrderExecutionCondition executionCondition;

    /**
     * Тип заявки
     */
    @NotNull
    private final OrderType type;

    // endregion

    // region final

    /**
     * Комментарий
     */
    @NotNull
    @Builder.Default
    private final String comment = "";

    /**
     * Время жизни заявки
     */
    @Nullable
    @Builder.Default
    private final LocalDateTime goodTill = null;

    // endregion

    @Override
    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }

    @NotNull
    public static NewOrderTransaction fromOrder(@NotNull Order order) {
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
