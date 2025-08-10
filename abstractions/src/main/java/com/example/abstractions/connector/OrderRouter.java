package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Абстракция для диспетчера заявок
 */
public interface OrderRouter extends TransactionMessageVisitor, AutoCloseable {
    /**
     * Список доступных для торговли счетов
     */
    @NotNull List<String> getAvailableAccounts();

    /**
     * Отправить транзакцию
     * @param transaction экземпляр транзакция для отправки
     */
    void sendTransaction(@NotNull Transaction transaction);
}
