package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.outgoing.Transaction;

import java.util.List;

public interface OrderRouter extends TransactionVisitor, AutoCloseable {
    List<String> getAvailableAccounts();

    void sendTransaction(Transaction transaction);
}
