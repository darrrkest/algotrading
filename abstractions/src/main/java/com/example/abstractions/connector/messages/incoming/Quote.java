package com.example.abstractions.connector.messages.incoming;

import java.math.BigDecimal;

public record Quote(BigDecimal price, int Size) {
    @Override
    public String toString() {
        return price + "(" + Size + ")";
    }
}
