package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("MoneyPosition")
public final class QLMoneyPosition extends QLMessage {

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.MONEY_POSITION;
    }

    @JsonProperty("firmid")
    String firmId;

    @JsonProperty("trdaccid")
    String account;

    @JsonProperty("cbplimit")
    double openPositionLimit;

    @JsonProperty("cbplused")
    double currentPosition;

    @JsonProperty("cbplplanned")
    double plannedPosition;

    @JsonProperty("varmargin")
    double varMargin;

    @JsonProperty("ts_comission")
    double commission;
}
