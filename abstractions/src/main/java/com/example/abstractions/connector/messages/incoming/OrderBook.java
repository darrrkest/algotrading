package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * Стакан
 */
@Getter
@SuperBuilder
public final class OrderBook extends InstrumentMessage {

    @NotNull
    private final List<OrderBookItem> items;

    /**
     * Биды
     */
    @NotNull
    public List<OrderBookItem> getBids() {
        return items.stream()
                .filter(n -> n.operation().equals(OrderOperation.BUY))
                .sorted(Comparator.comparing(OrderBookItem::price))
                .toList();
    }

    /**
     * Аски
     */
    @NotNull
    public List<OrderBookItem> getAsks() {
        return items.stream()
                .filter(n -> n.operation().equals(OrderOperation.SELL))
                .sorted(Comparator.comparing(OrderBookItem::price).reversed())
                .toList();
    }
}
