package com.example.abstractions.connector.messages.incoming;

public record Quote(double price, int Size) {
    @Override
    public String toString() {
        return price + "(" + Size + ")";
    }
}
