package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;
import org.jetbrains.annotations.NotNull;

/**
 * Результат подписки на обновление котировок
 *
 * @param instrument инструмент
 * @param success    флаг успешности
 * @param message    ошибка, при неудаче
 */
public record SubscriptionResult(@NotNull Instrument instrument, boolean success, @NotNull String message) {
    public static SubscriptionResult OK(@NotNull Instrument instrument) {
        return new SubscriptionResult(instrument, true, "");
    }

    public static SubscriptionResult Error(@NotNull Instrument instrument, String message) {
        return new SubscriptionResult(instrument, false, message);
    }
}

