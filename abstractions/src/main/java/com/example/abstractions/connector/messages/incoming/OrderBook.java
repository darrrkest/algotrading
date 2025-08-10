package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Стакан
 */
@Getter
@SuperBuilder
public class OrderBook extends InstrumentMessage {
    private @NotNull List<OrderBookItem> items;

    /**
     * Биды
     */
    public @Nullable List<OrderBookItem> getBids() {
        return items.stream()
                .filter(n -> n.getOperation().equals(OrderOperation.BUY))
                .sorted(Comparator.comparing(OrderBookItem::getPrice))
                .toList();
    }

    /**
     * Аски
     */
    public @Nullable List<OrderBookItem> getAsks() {
        return items.stream()
                .filter(n -> n.getOperation().equals(OrderOperation.SELL))
                .sorted(Comparator.comparing(OrderBookItem::getPrice).reversed())
                .toList();
    }
}
