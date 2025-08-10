package com.example.abstractions.connector;

/**
 * Статус подключения
 */
public enum ConnectionStatus {
    /**
     * Подключен
     */
    CONNECTED,

    /**
     * Подключается
     */
    CONNECTING,

    /**
     * Отключен
     */
    DISCONNECTED,

    /**
     * Отключается
     */
    DISCONNECTING,

    /**
     * Не определен
     */
    UNDEFINED;

    /**
     * Активен ли статус
     */
    public static boolean isActive(ConnectionStatus status) {
        return status == CONNECTED || status == CONNECTING;
    }
}
