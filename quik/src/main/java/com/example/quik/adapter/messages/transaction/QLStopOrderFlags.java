package com.example.quik.adapter.messages.transaction;

import lombok.Getter;

/**
 * Флаги стоп заявок
 */
@Getter
public enum QLStopOrderFlags {
    ACTIVE(0x1),
    CANCELLED(0x2),
    SELL(0x4),
    LIMIT(0x8),
    WAITING_FOR_ACTIVATION(0x20),
    ANOTHER_SERVER(0x40),
    MANUALLY_ACTIVATED(0x200),
    ACTIVATED_REJECTED_BY_EXCHANGE(0x400),
    ACTIVATED_REJECTED_LIMITS(0x800),
    CANCELLED_THUS_MAIN_ORDER_CANCELLED(0x1000),
    CANCELLED_THUS_MAIN_ORDER_FILLED(0x2000),
    MIN_MAX_IS_CALCULATING(0x8000);

    private final int value;

    QLStopOrderFlags(int value) {
        this.value = value;
    }
}
