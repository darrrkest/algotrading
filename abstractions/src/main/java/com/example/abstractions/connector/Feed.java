package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.incoming.InstrumentParams;
import com.example.abstractions.connector.messages.incoming.OrderBook;
import com.example.abstractions.symbology.Instrument;
import org.jetbrains.annotations.NotNull;

/**
 * Абстракция фида. Предоставляет методы подписки на различные типы данных
 */
public interface Feed extends AutoCloseable {
    /**
     * Подписаться на параметры инструмента {@link InstrumentParams}
     * @param instrument инструмент
     * @return Результат подписки
     */
    @NotNull
    SubscriptionResult subscribeParams(@NotNull Instrument instrument);

    /**
     * Отписаться от параметров инструмента {@link InstrumentParams}
     * @param instrument инструмент
     */
    void unsubscribeParams(@NotNull Instrument instrument);

    /**
     * Подписаться на стакан по инструменту {@link OrderBook}
     * @param instrument инструмент
     * @return Результат подписки
     */
    @NotNull
    SubscriptionResult subscribeOrderBook(@NotNull Instrument instrument);

    /**
     * Отписаться от стакан по инструменту  {@link OrderBook}
     * @param instrument инструмент
     */
    void unsubscribeOrderBook(@NotNull Instrument instrument);
}
