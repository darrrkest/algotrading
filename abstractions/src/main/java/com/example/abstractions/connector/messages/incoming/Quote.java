package com.example.abstractions.connector.messages.incoming;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record Quote(@NotNull BigDecimal price, int Size) {
    @Override
    public String toString() {
        return price + "(" + Size + ")";
    }
}
