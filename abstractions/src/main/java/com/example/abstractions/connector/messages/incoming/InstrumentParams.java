package com.example.abstractions.connector.messages.incoming;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@SuperBuilder
public class InstrumentParams extends InstrumentMessage {
    Quote bid;
    Quote ask;
    Quote last;

    private BigDecimal topPriceLimit;
    private BigDecimal bottomPriceLimit;

    private int decimalPlaces;

    private int lotSize;

    private BigDecimal priceStep;
    private BigDecimal priceStepValue;

    /**
     * Расчетная цена
     */
    private BigDecimal settlement;

    /**
     * Расчетная цена предыдущего торгового дня
     */
    private BigDecimal previousSettlement;

    private BigDecimal theorPrice;
    private BigDecimal IV;
    private LocalTime sessionEndTime;
    private int openInterest;
    private LocalDateTime lastUpdateTime;
    private LocalDate expireDate;

}
