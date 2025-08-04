package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("EnvAck")
public final class QLEnvelopeAcknowledgment extends QLMessage {
    @JsonProperty("id")
    private int id;

    public QLEnvelopeAcknowledgment(int id) {
        this.id = id;
    }

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ENV_ACK;
    }
}
