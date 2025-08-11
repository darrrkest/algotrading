package com.example.quik;

import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.connector.OrderRouterBase;
import com.example.abstractions.connector.messages.incoming.*;
import com.example.abstractions.connector.messages.outgoing.*;
import com.example.abstractions.execution.*;
import com.example.abstractions.symbology.Instrument;
import com.example.abstractions.symbology.InstrumentService;
import com.example.quik.adapter.QLAdapter;
import com.example.quik.adapter.messages.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QLRouterImpl extends OrderRouterBase implements QLRouter {

    private static final Logger log = LoggerFactory.getLogger(QLRouterImpl.class);
    private final InstrumentService instrumentService;
    private final QLAdapter adapter;
    private final QLOrdersContainer ordersContainer = new QLOrdersContainer();
    private final QLStopOrdersContainer stopOrdersContainer = new QLStopOrdersContainer();

    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicLong transId = new AtomicLong(0);
    private final Semaphore pendingSemaphore = new Semaphore(0);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QLRouterImpl(InstrumentService instrumentService, QLAdapter adapter, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.instrumentService = instrumentService;
        this.adapter = adapter;

        adapter.addMessageListener(this);

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
                pendingTransactionReplies.forEach(this::handle);

                var pendingFills = ordersContainer.getPendingFills();
                log.debug("Processing {} pending fills", pendingFills.size());
                for (var fill : pendingFills) {
                    handle(fill);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.debug("Stop pending message processing task");
    }

    @Override
    public void onMessageReceived(@NotNull QLMessage message) {
        switch (message.getMessageType()) {
            case TRANSACTION_REPLY -> handle((QLTransactionReply) message);
            case ORDER_STATE_CHANGE -> handle((QLOrderStateChange) message);
            case STOP_ORDER_STATE_CHANGE -> handle((QLStopOrderStateChange) message);
            case MONEY_POSITION -> handle((QLMoneyPosition) message);
            case POSITION -> handle((QLPosition) message);
            case ACCOUNTS_LIST -> handle((QLAccountsList) message);
            case FILL -> handle((QLFill) message);
            case INIT_END -> handle((QLInitEnd) message);
            case HEARTBEAT -> handle((QLHeartbeat) message);
        }
    }

    private void handle(QLStopOrderStateChange message) {
        log.debug("Handle QLStopOrderStateChange: {}", message);

        updateTransId(message.getTransId());

        var instrument = instrumentService.resolveInstrument(message.getSecCode(), ConnectorType.QUIK);
        if (instrument == null) {
            log.warn("Cannot resolve instrument: {}", message.getSecCode());
            return;
        }

        var transaction = stopOrdersContainer.getTransaction(message.getTransId(), message.getOrderExchangeId());

        raiseMessageReceived(this,
                StopOrderMessage.builder()
                        .stopOrder(createStopOrder(instrument, message, transaction))
                        .build());
    }

    private void handle(QLTransactionReply message) {
        log.debug("Handle QLTransactionReply: transId={} orderId={} account={} secCode={} status={} ",
                message.getTransId(),
                message.getOrderNum(),
                message.getAccount(),
                message.getSecCode(),
                message.getStatus()
        );

        var orderExchangeId = parseOrderIdFromTransactionReply(message);
        if (orderExchangeId > 0) {
            ordersContainer.putOrderExchangeId(orderExchangeId);
        }

        var pendingOscmToProcess = ordersContainer.putTransactionReply(message, orderExchangeId);

        if (pendingOscmToProcess.isPresent()) {
            var oscms = pendingOscmToProcess.get();
            log.debug("Handle(TransactionReply) fires {} pending OSCMs to process", oscms.size());

            for (var oscm : oscms) {
                if (oscm.getOrderExchangeId() == orderExchangeId && oscm.getTransId() == 0) {
                    oscm.setTransId(message.getTransId());
                }

                handle(oscm);
            }
        }

        preprocessTransactionReply(message);

        if (message.isSuccessful()) {
            processSuccessfulTransactionReply(message);
        } else {
            processFailedTransactionReply(message);
        }
    }

    private void handle(QLOrderStateChange message) {
        log.debug("Handle QLOrderStateChange: message {}", message);

        updateTransId(message.getTransId());
        Transaction transaction =
                ordersContainer.getNewOrderTransaction(message.getTransId(), message.getOrderExchangeId());

        if (transaction == null) {
            transaction = ordersContainer.getKillOrderTransactionByOrderId(message.getOrderExchangeId());
        }

        if (ordersContainer.hasUnRepliedTransactions()) {
            if (transaction == null) {
                if (message.getTransId() == 0 && !tryFixZeroTransactionId(message)) {
                    return;
                }
            }

            if (ordersContainer.isTransactionUnReplied(message.getTransId())) {
                if (message.getState() == OrderState.ACTIVE ||
                    message.getState() == OrderState.PARTIALLY_FILLED ||
                    message.getState() == OrderState.FILLED
                ) {
                    log.info(
                            "Handle QLOrderStateChange: transaction with trans_id = {} is pending " +
                                    "and order state {} received. Create and handle artificial QLTransactionReply",
                            message.getTransId(),
                            message.getState()
                    );

                    handle(QLTransactionReply.builder()
                            .status(3L)
                            .resultMsg(message.getRejectReason())
                            .transId(message.getTransId())
                            .account(message.getAccount())
                            .orderNum(message.getOrderExchangeId())
                            .price(message.getPrice())
                            .balance(message.getBalance())
                            .quantity(message.getQuantity())
                            .build());
                }
            }
        }

        if (ordersContainer.putOrderStateChange(message)) {
            // duplicate
            return;
        }

        var newTransaction = ordersContainer.getNewOrderTransaction(message.getTransId(), message.getOrderExchangeId());
        var killTransaction = ordersContainer.getKillOrderTransactionByOrderId(message.getOrderExchangeId());

        if (newTransaction != null || killTransaction != null) {
            processKnownOrderStateChange(
                    message,
                    newTransaction != null ? newTransaction.getTransactionId() : null,
                    killTransaction != null ? killTransaction.getTransactionId() : null);
        } else {
            ProcessUnknownOrderStateChange(message);
        }

        /// Гарантия одного разрешения
        if (pendingSemaphore.availablePermits() == 0) {
            pendingSemaphore.release();
        }
    }

    private void processKnownOrderStateChange(QLOrderStateChange message,
                                              @Nullable UUID newTransactionId,
                                              @Nullable UUID killTransactionId) {
        UUID transactionId = null;

        if (newTransactionId != null && killTransactionId != null) {
            transactionId = message.getState() == OrderState.CANCELLED ? newTransactionId : killTransactionId;

            log.debug(
                    "OSCM with two associated transactions received: not={}, kot={}. " +
                            "State is {}, select {} for further processing",
                    newTransactionId,
                    killTransactionId,
                    message.getState(),
                    transactionId
            );
        } else if (newTransactionId != null) {
            transactionId = newTransactionId;
        } else if (killTransactionId != null) {
            transactionId = killTransactionId;
        }

        raiseMessageReceived(
                this,
                OrderStateChangeMessage.builder()
                        .orderExchangeId(String.valueOf(message.getOrderExchangeId()))
                        .transactionId(transactionId)
                        .activeSize(message.getBalance())
                        .filledSize(message.getFilled())
                        .state(message.getState())
                        .changeTime(message.getTime())
                        .price(message.getPrice())
                        .build()
        );
    }

    private void ProcessUnknownOrderStateChange(QLOrderStateChange message) {
        try {
            var order = ordersContainer.getOrder(message.getOrderExchangeId());

            if (order == null) {
                var instrument = instrumentService.resolveInstrument(message.getSecCode(), ConnectorType.QUIK);
                if (instrument == null) {
                    log.warn("Cannot resolve instrument: {}", message.getSecCode());
                    return;
                }

                order = createOrder(instrument, message);
                ordersContainer.putOrder(message.getOrderExchangeId(), order);

                raiseMessageReceived(
                        this,
                        OrderMessage.builder()
                                .order(order)
                                .build()
                );

                raiseMessageReceived(this,
                        createOrderChange(
                                message,
                                order.getTransactionId()
                        )
                );
            } else {
                raiseMessageReceived(this,
                        createOrderChange(
                                message,
                                order.getTransactionId()
                        )
                );
            }
        } catch (Exception e) {
            log.error("Failed to process {}", message);
        }
    }

    private boolean tryFixZeroTransactionId(QLOrderStateChange message) {
        var transactionId = ordersContainer.tryFindInitiatingTransaction(message);

        if (transactionId < 0) {
            ordersContainer.putPendingOrderStateChange(message);
            return false;
        }

        message.setTransId(transactionId);
        return true;
    }

    private void handle(QLMoneyPosition message) {
        // TODO MoneyPosition
    }

    private void handle(QLPosition message) {
        try {
            log.debug("Handle QLPosition: message {}", message);

            var instrument = instrumentService.resolveInstrument(message.getSecCode(), ConnectorType.QUIK);

            if (instrument == null) {
                log.warn("Cannot resolve instrument: {}", message.getSecCode());
                return;
            }

            raiseMessageReceived(
                    this,
                    PositionMessage.builder()
                            .account(message.getAccount())
                            .instrument(instrument)
                            .quantity(message.getTotalNet())
                            .build()
            );
        } catch (Exception e) {
            log.error("Handle QLPosition: failed to process message {}", message);
        }
    }

    private void handle(QLAccountsList message) {
        log.trace("Handle QLAccountsList: {}", message);

        for (var account : message.getAccounts()) {
            addAccount(account);
        }
    }

    private void handle(QLFill message) {
        try {
            log.debug("Handle QLFill: {}", message);

            var instrument = instrumentService.resolveInstrument(message.getSecCode(), ConnectorType.QUIK);

            if (instrument == null) {
                log.warn("Cannot resolve instrument: {}", message.getSecCode());
                return;
            }

            if (ordersContainer.isCurrentSessionOrder(message.getOrderId())) {
                var lastOscm = ordersContainer.getLastOrderStateChangeForOrderId(message.getOrderId());
                if (lastOscm.isEmpty()) {
                    log.debug("Handle QLFill: FillId={} will be processed later, no OSCM received", message.getFillId());
                    ordersContainer.putPendingFill(message);
                    return;
                }
            } else if (ordersContainer.hasUnRepliedTransactions()) {
                log.debug("Handle QLFill: FillId={} will be processed later, there are unreplied transactions", message.getFillId());
                ordersContainer.putPendingFill(message);
                return;
            }

            if (!ordersContainer.putFill(message.getFillId())) {
                log.warn("Handle QLFill: FillId={} duplicate. Skipping", message.getFillId());
                return;
            }

            raiseMessageReceived(
                    this,
                    FillMessage.builder()
                            .instrument(instrument)
                            .account(message.getAccount())
                            .size(message.getSize())
                            .exchangeId(String.valueOf(message.getFillId()))
                            .exchangeOrderId(String.valueOf(message.getOrderId()))
                            .price(message.getPrice())
                            .operation(message.getOperation())
                            .dateTime(message.getTime())
                            .liquidityIndicator(message.getLiquidityIndicator())
                            .build()
            );
        }
        catch (Exception e) {
            log.error("Handle QLFill: failed to process message {}", message);
        }
    }

    private void handle(QLInitEnd message) {
        log.trace("Handle QLInitEnd: {}", message);

        if (initialized.get()) {
            log.warn("Handle QLInitEnd. Already initialized");
            return;
        }

        initialized.set(true);
    }

    private void handle(QLHeartbeat message) {
        raiseMessageReceived(this,
                SessionInfo.builder()
                        .serverTime(message.getTime())
                        .startTime(message.getStartTime())
                        .endTime(message.getEndTime())
                        .eveningStartTime(message.getEnvStartTime())
                        .eveningEndTime(message.getEnvEndTime())
                        .build());
    }

    private void preprocessTransactionReply(QLTransactionReply message) {
        if (message.getResultMsg() != null && (message.getResultMsg().contains("Вы не можете снять данную заявку") || message.getResultMsg().contains("Не найдена заявка для удаления"))) {
            log.warn("Failed kill transaction will be treated as successful");
        }

        message.setStatus(3);
    }

    private void processSuccessfulTransactionReply(QLTransactionReply message) {
        var newOrderTransaction = ordersContainer.getNewOrderTransaction(message.getTransId());
        var killOrderTransaction = ordersContainer.getKillOrderTransactionByTransId(message.getTransId());
        var modifyOrderTransaction = ordersContainer.getModifyOrderTransactionByTransId(message.getTransId());

        if (newOrderTransaction == null && killOrderTransaction == null && modifyOrderTransaction == null) {
            log.debug("Transaction reply received for transaction which wasn't sent by us {}", message);
            ordersContainer.removeProcessedPendingReply(message);
            return;
        }

        var lastOscm = ordersContainer.getLastOrderStateChangeForTransactionId(message);

        if (lastOscm.isEmpty()) {
            ordersContainer.putPendingTransactionReply(message);
            return;
        }

        var lastState = lastOscm.get().getState();

        if (killOrderTransaction != null && lastState == OrderState.NEW) {
            log.debug("Postpone kill TransactionReply. Last order state is {}", lastState);
            ordersContainer.putPendingTransactionReply(message);
            return;
        }

        if (newOrderTransaction != null &&
                (lastState == OrderState.NEW || lastState == OrderState.UNDEFINED)) {
            log.debug("Postpone new TransactionReply. Last order state is {}", lastState);
            ordersContainer.putPendingTransactionReply(message);
            return;
        }

        if (modifyOrderTransaction != null &&
                (lastState == OrderState.NEW || lastState == OrderState.UNDEFINED || lastOscm.get().getTransId() != message.getTransId())) {
            log.debug("Postpone modify TransactionReply. Last order state is {}", lastState);
            log.error("Handle TransactionReply: for modify transaction not implemented");
            ordersContainer.putPendingTransactionReply(message);
        }

        if (killOrderTransaction != null) {
            var unfilledQuantity = parseUnfilledQuantityFromTransactionReply(message);

            log.debug(
                    "Transaction reply received. Create artificial OSCM from successful kill transaction reply {}",
                    message.getTransId()
            );

            raiseMessageReceived(
                    this,
                    OrderStateChangeMessage.builder()
                            .orderExchangeId(String.valueOf(message.getTransId()))
                            .transactionId(killOrderTransaction.getTransactionId())
                            .activeSize(unfilledQuantity)
                            .state(OrderState.CANCELLED)
                            .build()
            );
        }

        var transId = newOrderTransaction != null
                ? newOrderTransaction.getTransactionId()
                : killOrderTransaction != null
                    ? killOrderTransaction.getTransactionId()
                    : modifyOrderTransaction.getTransactionId();

        raiseMessageReceived(
                this,
                TransactionReply.builder()
                        .success(message.isSuccessful())
                        .message(message.getResultMsg())
                        .transactionId(transId)
                        .build()
        );

        ordersContainer.removeProcessedPendingReply(message);
    }

    private void processFailedTransactionReply(QLTransactionReply message) {
        var newOrderTransaction = ordersContainer.getNewOrderTransaction(message.getTransId());
        var killOrderTransaction = ordersContainer.getKillOrderTransactionByTransId(message.getTransId());
        var modifyOrderTransaction = ordersContainer.getModifyOrderTransactionByTransId(message.getTransId());

        switch ((int) message.getStatus()) {
            case 13: log.error("Transaction failed due to cross trade"); break;
            default: log.error("Transaction failed {}", message); break;
        }

        var transId = newOrderTransaction != null
                ? newOrderTransaction.getTransactionId()
                : killOrderTransaction != null
                    ? killOrderTransaction.getTransactionId()
                    : modifyOrderTransaction != null
                        ? modifyOrderTransaction.getTransactionId()
                        : null;

        if (transId == null) {
            log.error("Can't find transaction associated with message {}", message);
            return;
        }

        raiseMessageReceived(
                this,
                TransactionReply.builder()
                        .success(message.isSuccessful())
                        .message(message.getResultMsg())
                        .transactionId(transId)
                        .build()
        );

        ordersContainer.removeProcessedPendingReply(message);
    }

    @NotNull
    private static OrderStateChangeMessage createOrderChange(QLOrderStateChange message, UUID transactionId) {
        return OrderStateChangeMessage.builder()
                .orderExchangeId(String.valueOf(message.getOrderExchangeId()))
                .transactionId(transactionId)
                .activeSize(message.getBalance())
                .filledSize(message.getFilled())
                .state(message.getState())
                .changeTime(message.getTime())
                .price(message.getPrice())
                .size(message.getQuantity())
                .build();
    }

    @NotNull
    private static Order createOrder(Instrument instrument, QLOrderStateChange message) {
        var order = new Order(
                message.getAccount(),
                instrument,
                message.getOperation(),
                message.getPrice(),
                message.getQuantity()
        );
        order.setOrderExchangeId(String.valueOf(message.getOrderExchangeId()));
        order.setState();
        order.setActiveSize(message.getBalance());
        order.setState();
        order.setState();
        return
        Order.builder()


                .activeSize(message.getBalance())
                .size(message.getQuantity())
                .state(message.getState())
                .price(message.getPrice())
                .type(OrderType.LIMIT)
                .comment(extractCommentFromBrokerRef(message.getBrokerRef()))
                .operation(message.getOperation())
                .dateTime(message.getTime())
                .transactionId(UUID.randomUUID())
                .build();
    }

    @NotNull
    private static StopOrder createStopOrder(Instrument instrument, QLStopOrderStateChange message, Transaction transaction) {
        StopOrderType type = switch (message.getStopOrderType()) {
            case SIMPLE_STOP_ORDER -> StopOrderType.STOP_LOSS;
            case ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER -> StopOrderType.STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER;
            case TAKE_PROFIT_STOP_ORDER -> StopOrderType.TAKE_PROFIT;
            case ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER -> StopOrderType.TAKE_PROFIT_ACTIVATED_BY_LIMIT_ORDER;
            case TAKE_PROFIT_AND_STOP_LIMIT_ORDER -> StopOrderType.TAKE_PROFIT_AND_STOP_LOSS;
            case ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER ->
                    StopOrderType.TAKE_PROFIT_AND_STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER;
            default -> throw new IllegalStateException("Unexpected value: " + message.getStopOrderType());
        };

        var stopLossPrice = message.getPrice();

        var stopLossTriggerPrice = switch (message.getStopOrderType()) {
            case SIMPLE_STOP_ORDER,
                 ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER -> message.getConditionPrice();
            case TAKE_PROFIT_STOP_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER,
                 TAKE_PROFIT_AND_STOP_LIMIT_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER -> message.getConditionPrice2();
            default -> throw new IllegalStateException("Unexpected value: " + message.getStopOrderType());
        };

        var takeProfitTriggerPrice = switch (message.getStopOrderType()) {
            case SIMPLE_STOP_ORDER,
                 ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER -> null;
            case TAKE_PROFIT_STOP_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER,
                 TAKE_PROFIT_AND_STOP_LIMIT_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER -> message.getConditionPrice();
            default -> throw new IllegalStateException("Unexpected value: " + message.getStopOrderType());
        };

        return StopOrder.builder()
                .state(message.getState())
                .account(message.getAccount())
                .orderExchangeId(String.valueOf(message.getOrderExchangeId()))
                .instrument(instrument)
                .operation(message.getOperation())
                .size(message.getQuantity())
                .type(type)
                .stopLossPrice(stopLossPrice)
                .stopLossTriggerPrice(stopLossTriggerPrice)
                .slippage(message.getSpread())
                .takeProfitTriggerPrice(takeProfitTriggerPrice)
                .takeProfitDeviation(message.getOffset())
                .activatingOrderId(String.valueOf(message.getCoOrderNum()))
                .transactionId(transaction != null ? transaction.getTransactionId() : null)
                .dateTime(message.orderDateTime.toLocalDateTime())
                .comment(message.getBrokerRef())
                .build();
    }

    private static String extractCommentFromBrokerRef(String brokerRef) {
        if (brokerRef == null || brokerRef.trim().isEmpty()) {
            return null;
        }

        int indexOfDelimiter = brokerRef.indexOf("//");

        return (indexOfDelimiter >= 0) ? brokerRef.substring(indexOfDelimiter + 2) : null;
    }

    private long parseOrderIdFromTransactionReply(@NotNull QLTransactionReply message) {
        if (message.getResultMsg() == null || message.getResultMsg().isEmpty()){
            return -1;
        }

        var matcher = Pattern.compile("^\\D*(\\d{5,}).*").matcher(message.getResultMsg());

        // Ровно одно совпадение
        if (!matcher.find() || matcher.groupCount() != 1) {
            return -1;
        }

        try {
            long value = Long.parseLong(matcher.group(1));
            log.debug("Parsed OrderId={}", value);
            return value;
        } catch (NumberFormatException e) {
            log.warn("Failed to parse OrderId from trans reply: {}", message.getResultMsg());
            return 0;
        }
    }

    private int parseUnfilledQuantityFromTransactionReply(QLTransactionReply message) {
        if (message.getResultMsg() == null || message.getResultMsg().isEmpty()){
            return -1;
        }

        Pattern pattern = message.getResultMsg().contains("Неисполненный остаток")
                ? Pattern.compile("^Заявка \\d{5,} снята\\. Неисполненный остаток: (?<quantity>\\d+)\\.")
                : Pattern.compile("^Заявка, с номером \\d{5,} снята\\. Снятое количество: (?<quantity>\\d+)\\.");

        Matcher matcher = pattern.matcher(message.getResultMsg());

        // Ровно одно совпадение
        if (!matcher.find() || matcher.groupCount() < 1) {
            return -1;
        }

        int value = -1;
        try {
            value = Integer.parseInt(matcher.group("quantity"));
        } catch (NumberFormatException e) {
            log.warn("Failed to parse unfilled quantity from trans reply: {}", message.getResultMsg());
        }

        log.debug("Parsed unfilled size is: {}", value);
        return value;
    }

    @Override
    protected void sendTransactionImpl(Transaction transaction) {
        transaction.accept(this);
    }

    private long incTransId() {
        return transId.incrementAndGet();
    }

    private void updateTransId(long transId) {
        if (transId > this.transId.get()) {
            this.transId.set(transId + 1);
        }
    }

    @Override
    public void visit(@NotNull NewOrderTransaction transaction) {
        var newOrderTransactionId = incTransId();
        var trans = QLTransaction.fromNewOrderTransaction(transaction, newOrderTransactionId);

        log.debug("visit NewOrderTransaction {}", trans);

        ordersContainer.putTransaction(newOrderTransactionId, transaction);
        adapter.sendMessage(trans);
    }

    @Override
    public void visit(@NotNull ModifyOrderTransaction transaction) {
        log.debug("visit ModifyOrderTransaction");
    }

    @Override
    public void visit(@NotNull KillOrderTransaction transaction) {
        var newOrderTransactionId = incTransId();
        var trans = QLTransaction.fromKillOrderTransaction(transaction, newOrderTransactionId);

        log.debug("visit KillOrderTransaction {}", trans);

        ordersContainer.putTransaction(newOrderTransactionId, transaction);
        adapter.sendMessage(trans);
    }

    @Override
    public void visit(@NotNull NewStopOrderTransaction transaction) {
        var newStopOrderTransactionId = incTransId();
        var trans = QLTransaction.fromNewStopOrderTransaction(transaction, newStopOrderTransactionId);

        log.debug("visit NewStopOrderTransaction {}", trans);

        stopOrdersContainer.putTransaction(newStopOrderTransactionId, transaction);
        adapter.sendMessage(trans);
    }

    @Override
    public void visit(@NotNull Transaction transaction) {
        raiseMessageReceived(this,
                TransactionReply.rejected(
                        transaction,
                        String.format("Transaction of type %s is not supported by router", transaction.getClass())
                )
        );
    }

    @Override
    public void close() throws Exception {
        cancelled.set(true);
        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }
}
