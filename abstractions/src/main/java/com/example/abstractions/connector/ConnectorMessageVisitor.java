package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.connector.messages.incoming.*;

public interface ConnectorMessageVisitor {
    void visit(TransactionReply message);
    void visit(FillMessage message);
    void visit(PositionMessage message);
    void visit(MoneyPosition message);
    void visit(InstrumentParams message);
    void visit(OrderBook message);

    /**
     * Прочие сообщения
     */
    void visit(ConnectorMessage message);
}