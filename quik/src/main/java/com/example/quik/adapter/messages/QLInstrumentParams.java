package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

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
    private double bidPrice;

    @JsonProperty("bidQuantity")
    private int bidSize;

    @JsonProperty("offer")
    private double askPrice;

    @JsonProperty("offerQuantity")
    private int askSize;

    @JsonProperty("last")
    private double lastPrice;

    @JsonProperty("priceStep")
    private double priceStep;

    @JsonProperty("time")
    private String time;

    @JsonProperty("strike")
    private double strike;

    @JsonProperty("volatility")
    private double volatility;

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
    private double settlement;

    @JsonProperty("openinterest")
    private int openInterest;

    @JsonProperty("previousSettlement")
    private double previousSettlement;

    @JsonProperty("stepPriceT")
    private double stepPriceT;

    @JsonProperty("stepPrice")
    private double stepPrice;

    @JsonProperty("stepPriceCl")
    private double stepPriceCl;

    @JsonProperty("stepPricePrCl")
    private double stepPricePrCl;

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
