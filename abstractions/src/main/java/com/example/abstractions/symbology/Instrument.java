package com.example.abstractions.symbology;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

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
    @NotNull
    private final String exchange;

    /**
     * Дата экспирации
     */
    @NotNull
    private final LocalDate expiration;

    /**
     * Тип инструмента
     */
    @Nullable
    private InstrumentType type;

    /**
     * Описание инструмента
     */
    @Nullable
    private String description;

    /**
     * Уникальный символ инструмента
     */
    @NotNull
    public String getSymbol() {
        return String.format("%s:%s", exchange, code);
    }

    public Instrument(@NotNull String code,
                      @NotNull String exchange,
                      @NotNull LocalDate expiration) {
        this.code = code;
        this.exchange = exchange;
        this.expiration = expiration;
    }

    public Instrument(@Nullable InstrumentType type,
                      @Nullable String description,
                      @NotNull String exchange,
                      @NotNull LocalDate expiration,
                      @NotNull String code) {
        this.type = type;
        this.description = description;
        this.exchange = exchange;
        this.expiration = expiration;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return Objects.equals(code, that.code) && Objects.equals(exchange, that.exchange) && Objects.equals(expiration, that.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, exchange, expiration);
    }
}

