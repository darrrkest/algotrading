package com.example.abstractions.connector.messages.incoming;

/**
 * Индикатор ликвидности
 */
public enum LiquidityIndicator {
    /**
     * Неопределенный индикатор
     */
    UNDEFINED,

    /**
     * Мейкер, пассивная сделка. Когда мы были в стакане и по нам ударили
     */
    MAKER,

    /**
     * Тейкер, активная сделка. Когда мы бьем по кому-то в стакане
     */
    TAKER
}
