package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Транзакция на модификацию заявки
 */
@Getter
@SuperBuilder
public final class ModifyOrderTransaction extends Transaction {

    // region required

    /**
     * Биржевой идентификатор заявки
     */
    @NotNull
    private final String orderExchangeId;

    /**
     * Новый объем заявки
     */
    private final int quantity;

    /**
     * Новая цена заявки
     */
    private final double price;

    // endregion

    // region final

    /**
     * Комментарий
     */
    @NotNull
    @Builder.Default
    private String comment = "";

    // endregion

    @Override
    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }
}
