package com.example.abstractions.connector.messages;

import com.example.abstractions.connector.messages.incoming.*;
import org.jetbrains.annotations.NotNull;

public interface ConnectorMessageVisitor {
    /**
     * Обработать транзакцию типа {@link TransactionReply}
     */
    void visit(@NotNull TransactionReply message);

    /**
     * Обработать транзакцию типа {@link FillMessage}
     */
    void visit(@NotNull FillMessage message);

    /**
     * Обработать транзакцию типа {@link PositionMessage}
     */
    void visit(@NotNull PositionMessage message);

    /**
     * Обработать транзакцию типа {@link MoneyPosition}
     */
    void visit(@NotNull MoneyPosition message);

    /**
     * Обработать транзакцию типа {@link InstrumentParams}
     */
    void visit(@NotNull InstrumentParams message);

    /**
     * Обработать транзакцию типа {@link OrderBook}
     */
    void visit(@NotNull OrderBook message);

    /**
     * Прочие сообщения
     */
    void visit(@NotNull ConnectorMessage message);
}