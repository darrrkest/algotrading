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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QLOrdersContainer {
    private static final Logger log = LoggerFactory.getLogger(QLOrdersContainer.class);
    private final Object lock = new Object();
    private final List<Long> currentSessionTransactionsIds = new ArrayList<>();
    private final List<Long> currentSessionOrderIds = new ArrayList<>();
    private final List<Long> newOrderTransactionsWithoutReplies = new ArrayList<>();

    private final Map<Long, NewOrderTransaction> mapQuikTransIdOnNewOrderTransaction = new HashMap<>();
    private final Map<Long, KillOrderTransaction> mapQuikTransIdOnKillOrderTransaction = new HashMap<>();
    private final Map<Long, KillOrderTransaction> mapOrderIdOnKillOrderTransaction = new HashMap<>();
    private final Map<Long, List<QLOrderStateChange>> orderStateChanges = new HashMap<>();
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
     * @return false, если статус уже приходил
     */
    public boolean putOrderStateChange(QLOrderStateChange osc) {

        try {
            int prevFilledQuantity, totalFilledQuantity;
            synchronized (lock) {
                if (!orderStateChanges.containsKey(osc.getOrderExchangeId())) {
                    orderStateChanges.put(osc.getOrderExchangeId(), new ArrayList<>());
                }

                var changes = orderStateChanges.get(osc.getOrderExchangeId());

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

                if (currentSessionTransactionsIds.contains(osc.getTransId()))
                {
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
}
