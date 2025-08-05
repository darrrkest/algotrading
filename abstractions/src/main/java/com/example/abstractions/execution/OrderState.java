package com.example.abstractions.execution;

public enum OrderState {
    UNDEFINED,

    NEW,

    ACTIVE,

    ERROR,

    PARTIALLY_FILLED,

    FILLED,

    CANCELLED;

}
