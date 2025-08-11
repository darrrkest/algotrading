package com.example.abstractions.connector.messages.outgoing;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Транзакция на модификацию заявки
 */
@Getter
@SuperBuilder
public final class ModifyOrderTransaction extends Transaction {

    /**
     * Биржевой идентификатор заявки
     */
    @NotNull
    private String orderExchangeId;

    /**
     * Новый объем заявки
     */
    private int quantity;

    /**
     * Новая цена заявки
     */
    private double price;

    /**
     * Комментарий
     */
    @NotNull
    private String comment;
}
