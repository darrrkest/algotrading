package com.example.connector.messages.incoming;

import com.example.execution.OrderOperation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.List;

@Getter
@SuperBuilder
public class OrderBook extends InstrumentMessage {
    List<OrderBookItem> items;

    public List<OrderBookItem> getBids() {
        return items.stream()
                .filter(n -> n.getOperation().equals(OrderOperation.BUY))
                .sorted(Comparator.comparing(OrderBookItem::getPrice))
                .toList();
    }

    public List<OrderBookItem> getAsks() {
        return items.stream()
                .filter(n -> n.getOperation().equals(OrderOperation.SELL))
                .sorted(Comparator.comparing(OrderBookItem::getPrice).reversed())
                .toList();
    }
}
