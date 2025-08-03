package com.example.messages.transaction;

import lombok.Getter;

@Getter
public enum QLStopOrderKind {
    SIMPLE_STOP_ORDER(1),

    CONDITION_PRICE_BY_OTHER_SEC(2),

    WITH_LINKED_LIMIT_ORDER(3),

    TAKE_PROFIT_STOP_ORDER(6),

    TAKE_PROFIT_AND_STOP_LIMIT_ORDER(9),

    ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER(7),

    ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER(8),

    ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER(10);

    private final int value;

    QLStopOrderKind(int value) {
        this.value = value;
    }
}
