package com.example.abstractions.execution;

/**
 * Состояние сторп заявки
 */
public enum StopOrderState {
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
     * Ждет активации по исполнению связной заявки
     */
    WAITING_FOR_ACTIVATION,

    /**
     * Расчитывается экстремум по тейкпрофиту
     */
    TRAILING_TAKE_PROFIT,

    /**
     * Ошибка в заявке
     */
    ERROR,

    /**
     * Заявка исполнена
     */
    FILLED,

    /**
     * Заявка снята
     */
    CANCELLED;
}
