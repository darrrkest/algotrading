package com.example.quik;

import com.example.abstractions.connector.messages.outgoing.NewStopOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class QLStopOrdersContainer {
    private static final Object lock = new Object();
    private static final Logger log = LoggerFactory.getLogger(QLStopOrdersContainer.class);
    public static final List<Long> currentSessionOrderIds = new ArrayList<>();

    public void putTransaction(long id, NewStopOrderTransaction transaction) {
        synchronized (lock) {
            currentSessionOrderIds.add(id);
        }
    }

    public Transaction getTransaction(long id, long exchangeTransactionId) {
        // TODO
        return null;
    }
}
