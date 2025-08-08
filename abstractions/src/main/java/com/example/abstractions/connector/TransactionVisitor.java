package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.Transaction;

public interface TransactionVisitor {
    void visit(NewOrderTransaction transaction);

    // TODO
    //void visit(ModifyOrderTransaction transaction);

    void visit(KillOrderTransaction transaction);

    // TODO
    //void visit(NewStopOrderTransaction transaction);

    void visit(Transaction transaction);
}
