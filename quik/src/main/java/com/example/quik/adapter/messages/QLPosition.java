package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonTypeName("Position")
public final class QLPosition extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.POSITION;
    }

    @JsonProperty("firmid")
    String firmId;

    @JsonProperty("trdaccid")
    String account;

    @JsonProperty("sec_code")
    String secCode;

    @JsonProperty("startnet")
    int startNet;

    @JsonProperty("totalnet")
    int totalNet;

    @JsonProperty("varmargin")
    double varMargin;

    @JsonProperty("avrposnprice")
    double avrPosnPrice;
}
