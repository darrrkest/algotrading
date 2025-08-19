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
    private final Quote bid;

    @Nullable
    private final Quote ask;

    @Nullable
    private final Quote last;

    /**
     * Максимально возможная цена
     */
    private final double topPriceLimit;

    /**
     * Минимально возможная цена
     */
    private final double bottomPriceLimit;

    /**
     * Точность цены
     */
    private final int decimalPlaces;

    /**
     * Размер одного лота
     */
    private final int lotSize;

    /**
     * Шаг цены инструмента
     */
    private final double priceStep;

    /**
     * Стоимость шага цены инструмента
     */
    private final double priceStepValue;

    /**
     * Расчетная цена
     */
    private final double settlement;

    /**
     * Расчетная цена предыдущего торгового дня
     */
    private final double previousSettlement;

    /**
     * Теоретическая цена
     */
    private final double theorPrice;

    /**
     * Волатильность опциона
     */
    private final double IV;

    /**
     * Время окончания торговой сессии
     */
    @NotNull
    private final LocalTime sessionEndTime;

    /**
     * Открытый интерес
     */
    private final int openInterest;

    /**
     * Время последнего обновления параметров
     */
    @NotNull
    private final LocalDateTime lastUpdateTime;

    /**
     * Дата экспирации инструмента
     */
    @NotNull
    private final LocalDate expireDate;

    /**
     * Номинал инструмента
     * <br>
     * NominalPrice = ( priceStep / priceStepValue) * previousSettlement;
     */
    private double nominal;
}
