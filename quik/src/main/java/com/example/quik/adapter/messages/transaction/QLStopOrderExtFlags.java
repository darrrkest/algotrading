package com.example.quik.adapter.messages.transaction;

import lombok.Getter;

@Getter
public enum QLStopOrderExtFlags {
    USE_MAIN_ORDER_UNFILLED_SIZE(0x1),

    CANCEL_STOP_IF_MAIN_ORDER_PARTIALLY_FILLED(0x2),

    ACTIVATE_STOP_ORDER_IF_MAIN_ORDER_PARTIALLY_FILLED(0x4),

    TAKE_PROFIT_SHIFT_UNIT_IS_PERCENTAGE(0x8),

    TAKE_PROFIT_PROTECTION_SPREAD_UNIT_IS_PERCENTAGE(0x10),

    STOP_ORDER_EXPIRY_IS_TODAY(0x20),

    STOP_ORDER_EXPIRY_IS_SET(0x40),

    TAKE_PROFIT_IS_MARKET_ORDER(0x80),

    STOP_LOSS_IS_MARKET_ORDER(0x100);

    private final int value;

    QLStopOrderExtFlags(int value) {
        this.value = value;
    }
}
