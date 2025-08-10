package com.example.abstractions.execution;

/**
 * Тип стоп заявки
 */
public enum StopOrderType {
    /**
     * Стоп-лосс
     */
    STOP_LOSS,

    /**
     * Тейк-профит
     */
    TAKE_PROFIT,

    /**
     * Тейк-профит и стоп-лосс
     */
    TAKE_PROFIT_AND_STOP_LOSS,

    /**
     * Стоп-лосс по исполнению заявки
     */
    STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER,

    /**
     * Тейк-профит по исполнению заявки
     */
    TAKE_PROFIT_ACTIVATED_BY_LIMIT_ORDER,

    /**
     * Тейк-профит и стоп-лосс по исполнению заявки
     */
    TAKE_PROFIT_AND_STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER
}
