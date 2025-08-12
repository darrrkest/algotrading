package com.example.quik;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.ModifyOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.execution.Order;
import com.example.abstractions.execution.OrderState;
import com.example.quik.adapter.messages.QLFill;
import com.example.quik.adapter.messages.QLOrderStateChange;
import com.example.quik.adapter.messages.QLTransactionReply;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс, знающий про все связи между транзакциями, заявками и сделками
 */
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
    private final Map<Long, ModifyOrderTransaction> mapQuikTransIdOnModifyOrderTransaction = new HashMap<>();
    private final Map<Long, List<QLOrderStateChange>> orderStateChanges = new HashMap<>();
    private final Map<Long, Long> mapOriginalOrderIdOnTransferredOrderId = new HashMap<>();
    private final Map<Long, QLFill> pendingFills = new HashMap<>();
    private final Map<Long, Long> mapQuikTransIdOnOrderExchangeId = new HashMap<>();
    private final Map<Long, Order> mapOrderIdOnOrder = new HashMap<>();

    private final Map<Long, List<QLOrderStateChange>> mapOrderIdOnPendingOrderStateChange = new HashMap<>();
    private final Map<Long, NewOrderTransaction> mapOrderIdOnNewOrderTransaction = new HashMap<>();
    private final List<QLTransactionReply> pendingTransactionReplies = new ArrayList<>();
    private HashSet<Long> processedFills = new HashSet<>();

    /**
     * Добавить в очередь ответ на транзакцию
     */
    public void putPendingTransactionReply(QLTransactionReply transactionReply) {
        synchronized (lock) {
            pendingTransactionReplies.add(transactionReply);
        }
    }

    /**
     * Сохранить сделку, требующую отложенной обработки
     */
    public void putPendingFill(QLFill fill) {
        synchronized (lock) {
            if (pendingFills.containsKey(fill.getFillId())) {
                log.warn("Duplicate fill received: {}", fill.getFillId());
                return;
            }

            pendingFills.put(fill.getFillId(), fill);
        }
    }

    /**
     * Сохранить заявку по её биржевому номеру
     */
    public void putOrder(long orderNum, Order order) {
        synchronized (lock) {
            mapOrderIdOnOrder.put(orderNum, order);
        }
    }

    /**
     * Сохранение OSCM, а также расчет реально исполненного количества в текущем PARTIALLY_FILLED изменении
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

    /**
     * Сохраняет биржевой номер заявки в коллекции своих (отправленных в текущую сессию работы) заявок
     */
    public void putOrderExchangeId(long orderExchangeId) {
        synchronized (lock) {
            currentSessionOrderIds.add(orderExchangeId);
        }
    }

    /**
     * Сохранить транзакцию на постановку заявки по её идентификатору
     */
    public void putTransaction(long id, NewOrderTransaction newOrderTransaction) {
        synchronized (lock) {
            currentSessionTransactionsIds.add(id);
            newOrderTransactionsWithoutReplies.add(id);
            mapQuikTransIdOnNewOrderTransaction.put(id, newOrderTransaction);
            mapQuikTransIdOnNewOrderTransactionTime.put(id, LocalDateTime.now());
        }
    }

    /**
     * Сохранить транзакцию на постановку заявки по её идентификатору
     */
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

    /**
     * Обработать ответ на постановку транзакции, возвращает список QLOrderStateChange, обработка которых была отложена
     */
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

    /**
     * Положить OSCM в отложенную обработку
     */
    public void putPendingOrderStateChange(QLOrderStateChange oscm) {
        synchronized (lock) {
            mapOrderIdOnPendingOrderStateChange
                    .computeIfAbsent(oscm.getOrderExchangeId(), k -> new ArrayList<>())
                    .add(oscm);
        }
    }

    /**
     * Сохранить номер сделки в список обработанных, чтобы не обрабатывать дубликаты. Возвращает true, если
     * сделка была сохранена ранее.
     */
    public boolean putFill(long fillId) {
        synchronized (lock) {
            return processedFills.add(fillId);
        }
    }

    /**
     * Возвращает копию коллекции ожидающих ответов на транзакции
     */
    public List<QLTransactionReply> getPendingTransactionReplies() {
        ArrayList<QLTransactionReply> values;

        synchronized (lock) {
            values = new ArrayList<>(pendingTransactionReplies);
            pendingTransactionReplies.clear();
        }

        return values;
    }

    /**
     * Возвращает копию коллекции ожидающих сделок
     *
     */
    public List<QLFill> getPendingFills() {
        ArrayList<QLFill> values;

        synchronized (lock) {
            values = new ArrayList<>(pendingFills.values());
            pendingFills.clear();
        }

        return values;
    }

    /**
     * Возвращает последний полученный статус заявки, связанный с транзакцией с идентификатором transId
     */
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

    /**
     * Возвращает последний OSCM для указанного биржевого номера заявки
     */
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

    /**
     * Получить транзакцию на постановку заявки по её идентификатору
     * @param transId идентификатор транзакции
     * @param orderId биржевой идентификатор заявки
     */
    @Nullable
    public NewOrderTransaction getNewOrderTransaction(long transId, long orderId) {
        synchronized (lock) {
            var value = mapQuikTransIdOnNewOrderTransaction.get(transId);
            if (value == null) {
                value = mapOrderIdOnNewOrderTransaction.get(orderId);
            }
            return value;
        }
    }

    /**
     * Получить транзакцию на постановку заявки по её идентификатору
     * @param transId идентификатор транзакции
     */
    @Nullable
    public NewOrderTransaction getNewOrderTransaction(long transId) {
        synchronized (lock) {
            return mapOrderIdOnNewOrderTransaction.get(transId);
        }
    }

    /**
     * Получить транзакцию на изменение заявки по её идентификатору (trans Id)
     */
    @Nullable
    public ModifyOrderTransaction getModifyOrderTransactionByTransId(long transId) {
        synchronized (lock) {
            return mapQuikTransIdOnModifyOrderTransaction.get(transId);
        }
    }

    /**
     * Получить транзакцию на снятие заявки по её идентификатору транзакции (trans Id)
     */
    @Nullable
    public KillOrderTransaction getKillOrderTransactionByTransId(long transId) {
        synchronized (lock) {
            return mapQuikTransIdOnKillOrderTransaction.get(transId);
        }
    }

    /**
     * Получить транзакцию на снятие заявки по идентификатору заявки
     */
    @Nullable
    public KillOrderTransaction getKillOrderTransactionByOrderId(long orderId) {
        synchronized (lock) {
            return mapOrderIdOnKillOrderTransaction.get(orderId);
        }
    }

    /**
     * Получить заявку по её биржевому номеру
     */
    @Nullable
    public Order getOrder(long orderNum) {
        synchronized (lock) {
            return mapOrderIdOnOrder.get(orderNum);
        }
    }

    /**
     * Возвращает true если заявка с биржевым номером orderExchangeId отправлялась в текущей сессии работы программы
     */
    public boolean isCurrentSessionOrder(long orderExchangeId) {
        synchronized (lock) {
            return currentSessionOrderIds.contains(orderExchangeId);
        }
    }

    /**
     * Удалить отложенный ответ на транзакцию
     */
    public void removeProcessedPendingReply(QLTransactionReply reply) {
        synchronized (lock) {
            pendingTransactionReplies.remove(reply);
        }
    }

    /**
     * Проверяет есть ли транзакции на постановку заявки, на которые ещё не пришёл ответ
     */
    public boolean hasUnRepliedTransactions() {
        synchronized (lock) {
            return !pendingTransactionReplies.isEmpty();
        }
    }

    /**
     * Возвращает true, если транзакция с идентификатором transactionId ещё не дождалась своего transactionReply
     */
    public boolean isTransactionUnReplied(long transactionId) {
        synchronized (lock) {
            return newOrderTransactionsWithoutReplies.contains(transactionId);
        }
    }

    /**
     * Метод пытается найти соответсвующую oscm NewOrderTransaction, которая пока не получила QlTransactionReply
     */
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

    /**
     * Проверяет, подходит ли транзакция
     */
    private boolean transactionMatchesOrderStateChange(
            QLOrderStateChange oscm,
            long transactionId,
            NewOrderTransaction transaction) {
        if (!Objects.equals(oscm.getOperation(), transaction.getOperation())) return false;
        if (oscm.getPrice() != transaction.getPrice()) return false;
        if (oscm.getQuantity() != transaction.getSize()) return false;

        if (!transaction.getInstrument().getCode().contains(oscm.getSecCode())) return false;
        if (!oscm.getBrokerRef().endsWith(transaction.getComment())) return false;

        LocalDateTime transactionTime = mapQuikTransIdOnNewOrderTransactionTime.get(transactionId);
        if (transactionTime == null) return false;

        // если разница во времени между отправкой транзакции и приходом oscm больше 30 секунд, то считаем, что
        // это не соответствующие друг другу объекты, а остальные параметры просто совпали
        Duration duration = Duration.between(transactionTime, LocalDateTime.now());
        return Math.abs(duration.getSeconds()) <= 30;
    }

    /**
     * Сопоставляет ID перенесённой заявки с ID исходной заявки
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
     * Возвращает ID перенесенной заявки по исходному ID
     * Если было несколько переносов (например, A → B → C), возвращается последний ID
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
     * Возвращает исходный ID заявки по ID перенесённой заявки
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
     * Получение внутреннего идентификатора заявки по её ID
     */
    public UUID getOriginalOrderTransactionId(long orderId) {
        synchronized (lock) {
            var transaction = mapOrderIdOnNewOrderTransaction.get(orderId);
            return transaction != null ? transaction.getTransactionId() : null;
        }
    }
}
