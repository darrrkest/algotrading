package com.example.abstractions.connector.messages.incoming;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Параметры инструмента
 */
@Getter
@SuperBuilder
public final class InstrumentParams extends InstrumentMessage {
    @Nullable
    private Quote bid;

    @Nullable
    private Quote ask;

    @Nullable
    private Quote last;

    /**
     * Максимально возможная цена
     */
    @NotNull
    private double topPriceLimit;

    /**
     * Минимально возможная цена
     */
    @NotNull
    private double bottomPriceLimit;

    /**
     * Точность цены
     */
    private int decimalPlaces;

    /**
     * Размер одного лота
     */
    private int lotSize;

    /**
     * Шаг цены инструмента
     */
    @NotNull
    private double priceStep;

    /**
     * Стоимость шага цены инструмента
     */
    @Nullable
    private double priceStepValue;

    /**
     * Расчетная цена
     */
    @NotNull
    private double settlement;

    /**
     * Расчетная цена предыдущего торгового дня
     */
    @NotNull
    private double previousSettlement;

    /**
     * Теоретическая цена
     */
    @NotNull
    private double theorPrice;

    /**
     * Волатильность опциона
     */
    @NotNull
    private double IV;

    /**
     * Воемя окончания торговой сессии
     */
    @NotNull
    private LocalTime sessionEndTime;

    /**
     * Открытый интерес
     */
    private int openInterest;

    /**
     * Время последнего обновления параметров
     */
    @NotNull
    private LocalDateTime lastUpdateTime;

    /**
     * Дата экспирации инструмента
     */
    @NotNull
    private LocalDate expireDate;

    /**
     * Номинал инструмента
     * <br>
     * NominalPrice = ( priceStep / priceStepValue) * previousSettlement;
     */
    @Nullable
    private double nominal;
}
