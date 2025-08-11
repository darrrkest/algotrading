package com.example.abstractions.connector.messages;

import com.example.abstractions.connector.messages.outgoing.*;
import org.jetbrains.annotations.NotNull;

public interface TransactionMessageVisitor {
    /**
     * Обработать транзакцию типа {@link NewOrderTransaction}
     */
    void visit(@NotNull NewOrderTransaction transaction);

    /**
     * Обработать транзакцию типа {@link ModifyOrderTransaction}
     */
    void visit(@NotNull ModifyOrderTransaction transaction);

    /**
     * Обработать транзакцию типа {@link KillOrderTransaction}
     */
    void visit(@NotNull KillOrderTransaction transaction);

    /**
     * Обработать транзакцию типа {@link NewStopOrderTransaction}
     */
    void visit(@NotNull NewStopOrderTransaction transaction);

    /**
     * Прочие сообщения
     */
    void visit(@NotNull Transaction transaction);
}
