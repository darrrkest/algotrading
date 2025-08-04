package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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
    BigDecimal openPositionLimit;

    @JsonProperty("cbplused")
    BigDecimal currentPosition;

    @JsonProperty("cbplplanned")
    BigDecimal plannedPosition;

    @JsonProperty("varmargin")
    BigDecimal varMargin;

    @JsonProperty("ts_comission")
    BigDecimal commission;
}
