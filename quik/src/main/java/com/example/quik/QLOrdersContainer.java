package com.example.quik;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.execution.Order;
import com.example.abstractions.execution.OrderState;
import com.example.quik.messages.QLFill;
import com.example.quik.messages.QLOrderStateChange;
import com.example.quik.messages.QLTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class QLOrdersContainer {
    private static final Logger log = LoggerFactory.getLogger(QLOrdersContainer.class);
    private final Object lock = new Object();
    private final List<Long> currentSessionTransactionsIds = new ArrayList<>();
    private final List<Long> currentSessionOrderIds = new ArrayList<>();
    private final List<Long> newOrderTransactionsWithoutReplies = new ArrayList<>();

    private final Map<Long, NewOrderTransaction> mapQuikTransIdOnNewOrderTransaction = new HashMap<>();
    private final Map<Long, LocalDateTime> mapQuikTransIdOnNewOrderTransactionTime = new HashMap<>();
    private final Map<Long, KillOrderTransaction> mapQuikTransIdOnKillOrderTransaction = new HashMap<>();
    private final Map<Long, KillOrderTransaction> mapOrderIdOnKillOrderTransaction = new HashMap<>();
    private final Map<Long, List<QLOrderStateChange>> orderStateChanges = new HashMap<>();
    private final Map<Long, Long> mapOriginalOrderIdOnTransferredOrderId = new HashMap<>();
    private final Map<Long, QLFill> pendingFills = new HashMap<>();
    private final Map<Long, Long> mapQuikTransIdOnOrderExchangeId = new HashMap<>();
    private final Map<Long, Order> mapOrderIdOnOrder = new HashMap<>();

    private final Map<Long, List<QLOrderStateChange>> mapOrderIdOnPendingOrderStateChange = new HashMap<>();
    private final Map<Long, NewOrderTransaction> mapOrderIdOnNewOrderTransaction = new HashMap<>();
    private final List<QLTransactionReply> pendingTransactionReplies = new ArrayList<>();

    public void putPendingTransactionReply(QLTransactionReply transactionReply) {
        synchronized (lock) {
            pendingTransactionReplies.add(transactionReply);
        }
    }

    public void putPendingFill(QLFill fill) {
        synchronized (lock) {
            if (pendingFills.containsKey(fill.getFillId())) {
                log.warn("Duplicate fill received: {}", fill.getFillId());
                return;
            }

            pendingFills.put(fill.getFillId(), fill);
        }
    }

    public void putOrder(long orderNum, Order order) {
        synchronized (lock) {
            mapOrderIdOnOrder.put(orderNum, order);
        }
    }

    /**
     * Сохранение OSCM, а такж расчет реально исполненного количества в текущем PARTIALLY_FILLED изменении
     *
     * @return false, если статус уже приходил
     */
    public boolean putOrderStateChange(QLOrderStateChange osc) {

        try {
            int prevFilledQuantity, totalFilledQuantity;
            synchronized (lock) {
                var changes = orderStateChanges.computeIfAbsent(osc.getOrderExchangeId(), k -> new ArrayList<>());

                if (changes.contains(osc)) {
                    return false;
                }

                prevFilledQuantity = changes.stream()
                        .filter(change -> change.getState() == OrderState.PARTIALLY_FILLED)
                        .mapToInt(QLOrderStateChange::getFilled)
                        .sum();
                totalFilledQuantity = osc.getQuantity() - osc.getBalance();
                osc.setFilled(totalFilledQuantity - prevFilledQuantity);

                changes.add(osc);

                if (currentSessionTransactionsIds.contains(osc.getTransId())) {
                    mapQuikTransIdOnOrderExchangeId.put(osc.getTransId(), osc.getOrderExchangeId());
                }
            }
        } catch (OutOfMemoryError e) {
            log.error("Too much OSCM {} and orders {}", orderStateChanges.size(), mapQuikTransIdOnOrderExchangeId.size());
            throw e;
        }

        return true;
    }

    public void putOrderExchangeId(long orderExchangeId) {
        synchronized (lock) {
            currentSessionOrderIds.add(orderExchangeId);
        }
    }

    public void putTransaction(long id, NewOrderTransaction newOrderTransaction) {
        synchronized (lock) {
            currentSessionTransactionsIds.add(id);
            newOrderTransactionsWithoutReplies.add(id);
            mapQuikTransIdOnNewOrderTransaction.put(id, newOrderTransaction);
            mapQuikTransIdOnNewOrderTransactionTime.put(id, LocalDateTime.now());
        }
    }

    public void putTransaction(long id, KillOrderTransaction killOrderTransaction) {
        synchronized (lock) {
            long orderExchangeId;

            try {
                orderExchangeId = Long.parseLong(killOrderTransaction.getOrderExchangeId());
            } catch (NumberFormatException e) {
                log.error("Can't save kill transaction. Unable to parse orderExchangeId: {}", killOrderTransaction.getOrderExchangeId());
                return;
            }

            currentSessionTransactionsIds.add(orderExchangeId);
            mapQuikTransIdOnKillOrderTransaction.put(id, killOrderTransaction);
            mapOrderIdOnKillOrderTransaction.put(orderExchangeId, killOrderTransaction);
            mapQuikTransIdOnOrderExchangeId.put(id, orderExchangeId);
        }
    }

    public Optional<List<QLOrderStateChange>> putTransactionReply(QLTransactionReply message, long orderExchangeId) {
        List<QLOrderStateChange> oscmToProcess = null;
        synchronized (lock) {
            if (!newOrderTransactionsWithoutReplies.contains(orderExchangeId)) return Optional.empty();
        }

        var newOrderTransaction = mapQuikTransIdOnNewOrderTransaction.get(orderExchangeId);
        newOrderTransactionsWithoutReplies.remove(orderExchangeId);

        mapOrderIdOnNewOrderTransaction.put(orderExchangeId, newOrderTransaction);

        if (mapOrderIdOnPendingOrderStateChange.containsKey(orderExchangeId)) {
            oscmToProcess = mapOrderIdOnPendingOrderStateChange.get(orderExchangeId);

            mapOrderIdOnPendingOrderStateChange.remove(orderExchangeId);

            for (var oscm : oscmToProcess) {
                oscm.setTransId(message.getTransId());
            }
        }

        if (newOrderTransactionsWithoutReplies.isEmpty() && !mapOrderIdOnPendingOrderStateChange.isEmpty()) {
            if (oscmToProcess == null) oscmToProcess = new ArrayList<>();

            for (var pendingOscms : mapOrderIdOnPendingOrderStateChange.values()) {
                oscmToProcess.addAll(pendingOscms);

                // TODO вроде можно без ↓
                pendingOscms.clear();
            }

            mapOrderIdOnPendingOrderStateChange.clear();
        }

        return Optional.ofNullable(oscmToProcess);
    }

    public void putPendingOrderStateChange(QLOrderStateChange oscm) {
        synchronized (lock) {
            mapOrderIdOnPendingOrderStateChange
                    .computeIfAbsent(oscm.getOrderExchangeId(), k -> new ArrayList<>())
                    .add(oscm);
        }
    }

    public List<QLTransactionReply> getPendingTransactionReplies() {
        ArrayList<QLTransactionReply> values;

        synchronized (lock) {
            values = new ArrayList<>(pendingTransactionReplies);
            pendingTransactionReplies.clear();
        }

        return values;
    }

    public List<QLFill> getPendingFills() {
        ArrayList<QLFill> values;

        synchronized (lock) {
            values = new ArrayList<>(pendingFills.values());
            pendingFills.clear();
        }

        return values;
    }

    public Optional<QLOrderStateChange> getLastOrderStateChangeForTransactionId(QLTransactionReply reply) {
        QLOrderStateChange value;

        synchronized (lock) {
            var orderId = mapQuikTransIdOnOrderExchangeId.get(reply.getTransId());
            if (orderId == null) {
                orderId = reply.getOrderNum();
            }

            var oscms = orderStateChanges.get(orderId);
            if (oscms == null || oscms.isEmpty()) {
                return Optional.empty();
            }

            value = oscms.get(oscms.size() - 1);
        }

        return Optional.ofNullable(value);
    }

    public Optional<QLOrderStateChange> getLastOrderStateChangeForOrderId(long orderId) {
        QLOrderStateChange value;

        synchronized (lock) {
            var oscms = orderStateChanges.get(orderId);
            if (oscms == null || oscms.isEmpty()) {
                return Optional.empty();
            }

            value = oscms.get(oscms.size() - 1);
        }

        return Optional.ofNullable(value);
    }

    public Optional<NewOrderTransaction> getNewOrderTransaction(long transId, long orderId) {
        synchronized (lock) {
            var value = mapQuikTransIdOnNewOrderTransaction.get(transId);
            if (value == null) {
                value = mapOrderIdOnNewOrderTransaction.get(orderId);
            }
            return Optional.ofNullable(value);
        }
    }

    public Optional<NewOrderTransaction> getNewOrderTransaction(long transId) {
        synchronized (lock) {
            var value = mapOrderIdOnNewOrderTransaction.get(transId);
            return Optional.ofNullable(value);
        }
    }

    public Optional<KillOrderTransaction> getKillOrderTransactionByTransId(long transId) {
        synchronized (lock) {
            var value = mapQuikTransIdOnKillOrderTransaction.get(transId);
            return Optional.ofNullable(value);
        }
    }

    public Optional<KillOrderTransaction> getKillOrderTransactionByOrderId(long orderId) {
        synchronized (lock) {
            var value = mapOrderIdOnKillOrderTransaction.get(orderId);
            return Optional.ofNullable(value);
        }
    }

    public Optional<Order> getOrder(long orderNum) {
        synchronized (lock) {
            var value = mapOrderIdOnOrder.get(orderNum);
            return Optional.ofNullable(value);
        }
    }

    public boolean isCurrentSessionOrder(long orderExchangeId) {
        synchronized (lock) {
            return currentSessionOrderIds.contains(orderExchangeId);
        }
    }

    public void removeProcessedPendingReply(QLTransactionReply reply) {
        synchronized (lock) {
            pendingTransactionReplies.remove(reply);
        }
    }

    public boolean hasUnRepliedTransactions() {
        synchronized (lock) {
            return !pendingTransactionReplies.isEmpty();
        }
    }

    public boolean isTransactionUnReplied(long transactionId) {
        synchronized (lock) {
            return newOrderTransactionsWithoutReplies.contains(transactionId);
        }
    }

    public long tryFindInitiatingTransaction(QLOrderStateChange oscm) {
        synchronized (lock) {
            List<Map.Entry<Long, NewOrderTransaction>> unrepliedTransactions = new ArrayList<>();

            for (Long transId : newOrderTransactionsWithoutReplies) {
                NewOrderTransaction trans = mapQuikTransIdOnNewOrderTransaction.get(transId);
                if (trans != null) {
                    unrepliedTransactions.add(Map.entry(transId, trans));
                }
            }

            List<Map.Entry<Long, NewOrderTransaction>> matchingTransactions = unrepliedTransactions.stream()
                    .filter(entry -> transactionMatchesOrderStateChange(oscm, entry.getKey(), entry.getValue()))
                    .toList();

            if (matchingTransactions.size() == 1) {
                return matchingTransactions.get(0).getKey();
            }

            return Long.MIN_VALUE;
        }
    }

    private boolean transactionMatchesOrderStateChange(
            QLOrderStateChange oscm,
            long transactionId,
            NewOrderTransaction transaction
    ) {
        if (!Objects.equals(oscm.getOperation(), transaction.getOperation())) return false;
        if (oscm.getPrice() != transaction.getPrice()) return false;
        if (oscm.getQuantity() != transaction.getSize()) return false;

        if (!transaction.getInstrument().getCode().contains(oscm.getSecCode())) return false;
        if (!oscm.getBrokerRef().endsWith(transaction.getComment())) return false;

        LocalDateTime transactionTime = mapQuikTransIdOnNewOrderTransactionTime.get(transactionId);
        if (transactionTime == null) return false;

        Duration duration = Duration.between(transactionTime, LocalDateTime.now());
        return Math.abs(duration.getSeconds()) <= 30;
    }

    /**
     * Сопоставляет ID перенесённой заявки с ID исходной заявки.
     */
    public void mapOrderOnOriginalId(String originalId, Long transferredId) {
        long id;
        try {
            id = Long.parseLong(originalId);
        } catch (NumberFormatException e) {
            id = 0L;
        }

        synchronized (lock) {
            mapOriginalOrderIdOnTransferredOrderId.put(id, transferredId);
        }
    }

    /**
     * Возвращает ID перенесенной заявки по исходному ID.
     * Если было несколько переносов (например, A → B → C), возвращается последний ID.
     */
    public String getTransferredOrderId(String originalId) {
        long currentId;
        try {
            currentId = Long.parseLong(originalId);
        } catch (NumberFormatException e) {
            return originalId;
        }

        synchronized (lock) {
            while (true) {
                Long transferId = mapOriginalOrderIdOnTransferredOrderId.get(currentId);
                if (transferId == null) {
                    return Long.toString(currentId);
                }
                currentId = transferId;
            }
        }
    }

    /**
     * Возвращает исходный ID заявки по ID перенесённой заявки.
     */
    public String getOriginalOrderIdByTransferredId(long transferredOrderId) {
        synchronized (lock) {
            while (true) {
                Long finalTransferredOrderId = transferredOrderId;
                Optional<Map.Entry<Long, Long>> kvp = mapOriginalOrderIdOnTransferredOrderId.entrySet()
                        .stream()
                        .filter(entry -> Objects.equals(entry.getValue(), finalTransferredOrderId))
                        .findFirst();

                if (kvp.isEmpty()) {
                    return Long.toString(transferredOrderId);
                }

                transferredOrderId = kvp.get().getKey();
            }
        }
    }

    /**
     * Получение внутреннего идентификатора заявки по её ID.
     */
    public UUID getOriginalOrderTransactionId(long orderId) {
        synchronized (lock) {
            var transaction = mapOrderIdOnNewOrderTransaction.get(orderId);
            return transaction != null ? transaction.getTransactionId() : null;
        }
    }
}
