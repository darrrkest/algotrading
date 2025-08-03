package com.example.connector;

import com.example.connector.messages.outgoing.KillOrderTransaction;
import com.example.connector.messages.outgoing.NewOrderTransaction;
import com.example.connector.messages.outgoing.Transaction;

public interface TransactionVisitor {
    void visit(NewOrderTransaction transaction);

    //void visit(ModifyOrderTransaction transaction);

    void visit(KillOrderTransaction transaction);

    //void visit(NewStopOrderTransaction transaction);

    void visit(Transaction transaction);
}
