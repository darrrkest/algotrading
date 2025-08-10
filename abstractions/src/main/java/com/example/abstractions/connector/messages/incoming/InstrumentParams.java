package com.example.abstractions.connector.messages.incoming;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Параметры инструмента
 */
@Getter
@SuperBuilder
public class InstrumentParams extends InstrumentMessage {
    private @Nullable Quote bid;
    private @Nullable Quote ask;
    private @Nullable Quote last;

    /**
     * Максимально возможная цена
     */
    private @NotNull BigDecimal topPriceLimit;

    /**
     * Минимально возможная цена
     */
    private @NotNull BigDecimal bottomPriceLimit;

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
    private @NotNull BigDecimal priceStep;

    /**
     * Стоимость шага цены инструмента
     */
    private @Nullable BigDecimal priceStepValue;

    /**
     * Расчетная цена
     */
    private @NotNull BigDecimal settlement;

    /**
     * Расчетная цена предыдущего торгового дня
     */
    private @NotNull BigDecimal previousSettlement;

    /**
     * Теоретическая цена
     */
    private @NotNull BigDecimal theorPrice;

    /**
     * Волатильность опциона
     */
    private @NotNull BigDecimal IV;

    /**
     * Воемя окончания торговой сессии
     */
    private @NotNull LocalTime sessionEndTime;

    /**
     * Открытый интерес
     */
    private int openInterest;

    /**
     * Время последнего обновления параметров
     */
    private @NotNull LocalDateTime lastUpdateTime;

    /**
     * Дата экспирации инструмента
     */
    private @NotNull LocalDate expireDate;

    /**
     * Номинал инструмента
     * <br>
     * NominalPrice = ( priceStep / priceStepValue) * previousSettlement;
     */
    private @Nullable BigDecimal nominal;
}
