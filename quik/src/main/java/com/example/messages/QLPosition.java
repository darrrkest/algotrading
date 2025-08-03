package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

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
    String instrument;

    @JsonProperty("startnet")
    int startNet;

    @JsonProperty("totalnet")
    int totalNet;

    @JsonProperty("varmargin")
    double varMargin;
}
