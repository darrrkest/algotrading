package com.example.abstractions.symbology;

import lombok.Builder;
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
    @NotNull
    private final String code;

    /**
     * Код биржи, на которой торгуется
     */
    @Nullable
    private String exchange;

    /**
     * Описание инструмента
     */
    @Nullable
    private String description;

    /**
     * Тип инструмента
     */
    @Nullable
    private InstrumentType type;

    /**
     * Дата экспирации
     */
    @Nullable
    private LocalDate expiration;

    /**
     * Уникальный символ инструмента
     */
    @NotNull
    public String getSymbol() {
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

