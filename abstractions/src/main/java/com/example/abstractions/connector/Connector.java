package com.example.abstractions.connector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Абстракции коннектора
 */
public interface Connector extends AdapterConnectionStatusChangeListener, AutoCloseable {
    /**
     * Тип коннектора
     */
    @NotNull ConnectorType getType();

    /**
     * Текущий статус
     */
    @NotNull ConnectionStatus getConnectionStatus();

    /**
     * Фид данных
     */
    @NotNull Feed getFeed();

    /**
     * Получить роутер заявок. Nullable т.к. коннектор не всегда поддерживает размещение заявок
     */
    @Nullable OrderRouter getRouter();

    /**
     * Запустить
     */
    void start();

    /**
     * Остановить
     */
    void stop();
}
