package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("InstrumentParamsSubscriptionRequest")
public final class QLInstrumentParamsSubscriptionRequest extends QLMessage {

    @JsonProperty("instrument")
    private String instrument;

    public QLInstrumentParamsSubscriptionRequest(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INSTRUMENT_PARAMS_SUBSCRIPTION_REQUEST;
    }
}
