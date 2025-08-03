package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("InstrumentParamsUnsubscriptionRequest")
public final class QLInstrumentParamsUnsubscriptionRequest extends QLMessage {
    private String instrument;

    public QLInstrumentParamsUnsubscriptionRequest(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INSTRUMENT_PARAMS_UNSUBSCRIPTION_REQUEST;
    }
}

