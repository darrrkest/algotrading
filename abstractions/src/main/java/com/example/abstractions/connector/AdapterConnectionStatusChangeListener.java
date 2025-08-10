package com.example.abstractions.connector;

import org.jetbrains.annotations.NotNull;

/**
 * Слушатель изменений состояния подключения адаптера
 */
@FunctionalInterface
public interface AdapterConnectionStatusChangeListener {
    void onConnectionStatusChange(@NotNull ConnectionStatus status);
}
