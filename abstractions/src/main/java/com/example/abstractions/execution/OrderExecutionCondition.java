package com.example.abstractions.execution;

/**
 * Условие исполнения заявки
 */
public enum OrderExecutionCondition {

    /**
     * Неопределенное значение
     */
    UNDEFINED,

    /**
     * Отправить заявку в очередь
     */
    PUT_UN_QUEUE,

    /**
     * Снять заявку, если не исполнелась немедленно
     */
    FILL_OR_KILL,

    /**
     * Снять остаток
     */
    KILL_BALANCE

}
