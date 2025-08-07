package com.example.quik;

import com.example.abstractions.connector.OrderRouterBase;
import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import com.example.abstractions.symbology.InstrumentService;
import com.example.quik.adapter.QLAdapter;
import com.example.quik.adapter.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class QLRouterImpl extends OrderRouterBase implements QLRouter {

    private static final Logger log = LoggerFactory.getLogger(QLRouterImpl.class);
    private final InstrumentService instrumentService;
    private final QLAdapter adapter;
    private final QLOrdersContainer ordersContainer = new QLOrdersContainer();

    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicLong transId = new AtomicLong(0);
    private final Semaphore pendingSemaphore = new Semaphore(0);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QLRouterImpl(InstrumentService instrumentService, QLAdapter adapter, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.instrumentService = instrumentService;
        this.adapter = adapter;

        executor.submit(this::runProcessingPendingMessages);
    }

    private void runProcessingPendingMessages() {
        Thread.currentThread().setName("QL_PENDING_MSG");
        log.debug("Starting processing pending transactions");

        while (!cancelled.get()) {
            try {
                pendingSemaphore.acquire();
                if (cancelled.get()) break;

                var pendingTransactionReplies = ordersContainer.getPendingTransactionReplies();
                log.debug("Processing {} pending transaction replies", pendingTransactionReplies.size());
                pendingTransactionReplies.forEach(this::Handle);

                var pendingFills = ordersContainer.getPendingFills();
                log.debug("Processing {} pending fills", pendingFills.size());
                for (var fill : pendingFills) {
                    Handle(fill);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.debug("Stop pending message processing task");
    }

    @Override
    protected void sendTransactionImpl(Transaction transaction) {

    }

    @Override
    public void visit(NewOrderTransaction transaction) {

    }

    @Override
    public void visit(KillOrderTransaction transaction) {

    }

    @Override
    public void visit(Transaction transaction) {

    }

    @Override
    public void onMessageReceived(QLMessage message) {
        switch (message.getMessageType()) {
            case TRANSACTION_REPLY -> Handle((QLTransactionReply) message);
            case ORDER_STATE_CHANGE -> Handle((QLOrderStateChange) message);
            case MONEY_POSITION -> Handle((QLMoneyPosition) message);
            case POSITION -> Handle((QLPosition) message);
            case ACCOUNTS_LIST -> Handle((QLAccountsList) message);
            case FILL -> Handle((QLFill) message);
            case INIT_END -> Handle((QLInitEnd) message);
            case HEARTBEAT -> Handle((QLHeartbeat) message);
        }
    }

    private void Handle(QLTransactionReply message) {

    }

    private void Handle(QLOrderStateChange message) {

        /// Гарантия одного разрешения
        if (pendingSemaphore.availablePermits() == 0) {
            pendingSemaphore.release();
        }
    }

    private void Handle(QLMoneyPosition message) {

    }

    private void Handle(QLPosition message) {

    }

    private void Handle(QLAccountsList message) {

    }

    private void Handle(QLFill message) {

    }

    private void Handle(QLInitEnd message) {

    }

    private void Handle(QLHeartbeat message) {

    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }
}
