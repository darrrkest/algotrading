package com.example.abstractions.connector.messages.incoming.money;

import com.example.abstractions.connector.messages.ConnectorMessageVisitor;
import com.example.abstractions.connector.messages.incoming.AccountMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Информация о лимитах по деньгам по заданному счету.
 */
@Getter
@SuperBuilder
public final class MoneyPosition extends AccountMessage {

    private final Properties properties = new Properties();

    /**
     * Получить свойство по имени
     */
    @Nullable
    public synchronized String get(@NotNull String name) {
        return properties.getProperty(name);
    }

    /**
     * Установить свойство по имени
     */
    public synchronized void set(@NotNull String name, @NotNull Object value) {
        properties.put(name, value.toString());
    }

    /**
     * Получить все свойства (копия)
     */
    public synchronized Properties getAll() {
        Properties copy = new Properties();
        copy.putAll(this.properties);
        return copy;
    }

    /**
     * Обновить текущую позицию
     */
    public synchronized void update(@Nullable MoneyPosition update) {
        if (update == null) {
            return;
        }
        this.properties.putAll(update.getAll());
    }

    @Override
    public void accept(ConnectorMessageVisitor visitor) {
        visitor.visit(this);
    }
}
