package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@JsonTypeName("InstrumentParams")
public final class QLInstrumentParams extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INSTRUMENT_PARAMS;
    }

    @JsonProperty("code")
    private String code;

    @JsonProperty("classCode")
    private String classCode;

    @JsonProperty("fullCode")
    private String fullCode;

    @JsonProperty("bid")
    private BigDecimal bidPrice;

    @JsonProperty("bidQuantity")
    private int bidSize;

    @JsonProperty("offer")
    private BigDecimal askPrice;

    @JsonProperty("offerQuantity")
    private int askSize;

    @JsonProperty("last")
    private BigDecimal lastPrice;

    @JsonProperty("priceStep")
    private BigDecimal priceStep;

    @JsonProperty("time")
    private String time;

    @JsonProperty("strike")
    private BigDecimal strike;

    @JsonProperty("volatility")
    private BigDecimal volatility;

    @JsonProperty("lotsize")
    private int lotSize;

    @JsonProperty("optiontype")
    private String optionType;

    @JsonProperty("optionbase")
    private String optionBase;

    @JsonProperty("pricemax")
    private String priceMax;

    @JsonProperty("pricemin")
    private String priceMin;

    @JsonProperty("settlement")
    private BigDecimal settlement;

    @JsonProperty("openinterest")
    private int openInterest;

    @JsonProperty("previousSettlement")
    private BigDecimal previousSettlement;

    @JsonProperty("stepPriceT")
    private BigDecimal stepPriceT;

    @JsonProperty("stepPrice")
    private BigDecimal stepPrice;

    @JsonProperty("stepPriceCl")
    private BigDecimal stepPriceCl;

    @JsonProperty("stepPricePrCl")
    private BigDecimal stepPricePrCl;

    @JsonProperty("endTime")
    private LocalTime endTime = LocalTime.of(23, 59);

    @JsonProperty("expiredate")
    private String expireDate;

    @Override
    public String toString() {
        return "QLInstrumentParams{" +
                "code='" + code + '\'' +
                ", fullCode='" + fullCode + '\'' +
                ", bidPrice=" + bidPrice +
                ", bidSize=" + bidSize +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                ", lastPrice=" + lastPrice +
                ", priceStep=" + priceStep +
                ", time='" + time + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }
}
