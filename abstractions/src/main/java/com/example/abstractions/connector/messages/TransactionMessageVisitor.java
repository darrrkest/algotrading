package com.example.abstractions.connector.messages;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import org.jetbrains.annotations.NotNull;

public interface TransactionMessageVisitor {
    /**
     * Обработать транзакцию типа {@link NewOrderTransaction}
     */
    void visit(@NotNull NewOrderTransaction transaction);

    // TODO
    //void visit(ModifyOrderTransaction transaction);

    /**
     * Обработать транзакцию типа {@link KillOrderTransaction}
     */
    void visit(@NotNull KillOrderTransaction transaction);

    // TODO
    //void visit(NewStopOrderTransaction transaction);

    /**
     * Прочие сообщения
     */
    void visit(@NotNull Transaction transaction);
}
