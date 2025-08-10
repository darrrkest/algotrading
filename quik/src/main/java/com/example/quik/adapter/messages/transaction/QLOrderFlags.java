package com.example.quik.adapter.messages.transaction;

import lombok.Getter;

/**
 * Флаги для таблицы "Заявки"
 */
@Getter
public enum QLOrderFlags {
    ACTIVE(0x1),
    CANCELLED(0x2),
    SELL(0x4),
    LIMIT(0x8),
    FILL_AT_DIFFERENT_PRICES(0x10),
    FILL_OR_KILL(0x20),
    MARKET_MAKER_ORDER(0x40),
    HIDDEN(0x80),
    CANCEL_UNFILLED(0x100),
    ICEBERG(0x200),
    REJECTED(0x400),
    LINKEDORDER(0x10000);

    private final int value;

    QLOrderFlags(int value) {
        this.value = value;
    }
}
