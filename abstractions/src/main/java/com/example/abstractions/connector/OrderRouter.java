package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.outgoing.Transaction;

import java.util.List;

public interface OrderRouter {
    List<String> getAvailableAccounts();

    // TODO account added

    void sendTransaction(Transaction transaction);
}
