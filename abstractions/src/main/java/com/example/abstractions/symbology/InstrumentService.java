package com.example.abstractions.symbology;

import com.example.abstractions.connector.ConnectorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса, который знает с какими инструментами работает система
 */
public interface InstrumentService {
    /**
     * Список инструментов с которыми работает система
     */
    @NotNull
     List<Instrument> getKnownInstruments();

    /**
     * @return true, если инструмент изместен системе
     */
    boolean isInstrumentKnown(@NotNull Instrument instrument);

    /**
     * Возвращает тип инструмента
     */
    InstrumentType getInstrumentType(@NotNull Instrument instrument);

    /**
     * Возвращает код биржи на которой торгуется инструмент
     */
    String getInstrumentExchange(@NotNull Instrument instrument);

    /**
     * Возвращает инструмент, если он настроен в системе. Иначе null
     */
    @Nullable
    Instrument resolveInstrument(@NotNull String symbol, @NotNull String exchange);
}
