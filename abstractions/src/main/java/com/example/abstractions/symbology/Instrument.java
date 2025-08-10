package com.example.abstractions.symbology;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

/**
 * Дескриптор инструмента
 */
@Getter
public class Instrument {

    /**
     * Код инструмента
     */
    private final @NotNull String code;

    /**
     * Код биржи, на которой торгуется
     */
    private @Nullable String exchange;

    /**
     * Описание инструмента
     */
    private @Nullable String description;

    /**
     * Тип инструмента
     */
    private @Nullable InstrumentType type;

    /**
     * Дата экспирации
     */
    private @Nullable LocalDate expiration;

    /**
     * Уникальный символ инструмента
     */
    public @NotNull String getSymbol() {
        return exchange == null ? code : String.format("%s:%s", exchange, code);
    }

    public Instrument(@NotNull String code) {
        this.code = code;
    }

    public Instrument(@Nullable InstrumentType type,
                      @Nullable String description,
                      @Nullable String exchange,
                      @Nullable LocalDate expiration,
                      @NotNull String code) {
        this.type = type;
        this.description = description;
        this.exchange = exchange;
        this.expiration = expiration;
        this.code = code;
    }
}

