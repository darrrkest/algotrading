package com.example.execution;

import com.example.symbology.Instrument;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public final class Order {
    String account;
    Instrument instrument;
    OrderOperation operation;
    double price;
    int size;
    OrderType type = OrderType.LIMIT;
    int activeSize;
    String OrderExchangeId;
    UUID transactionId;
    OrderState state;
    LocalDateTime goodTill;
    LocalDateTime dateTime;
    String comment;

    @Override
    public String toString() {
        return "Order{" +
                "account='" + account + '\'' +
                ", instrument=" + instrument +
                ", operation=" + operation +
                ", price=" + price +
                ", size=" + size +
                ", activeSize=" + activeSize +
                ", type=" + type +
                ", OrderExchangeId='" + OrderExchangeId + '\'' +
                ", transactionId=" + transactionId +
                ", state=" + state +
                '}';
    }
}
