package com.example.abstractions.execution;

/**
 * Состояние заявки
 */
public enum OrderState {
    /**
     * Неопределенное значение
     */
    UNDEFINED,

    /**
     * Новая заявка
     */
    NEW,

    /**
     * Активная заявка
     */
    ACTIVE,

    /**
     * Ошибка в заявке
     */
    ERROR,

    /**
     * Заявка частично исполнена
     */
    PARTIALLY_FILLED,

    /**
     * Заявка исполнена
     */
    FILLED,

    /**
     * Заявка снята
     */
    CANCELLED;

}
