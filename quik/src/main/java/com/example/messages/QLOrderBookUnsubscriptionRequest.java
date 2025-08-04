package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("OrderBookUnsubscriptionRequest")
public class QLOrderBookUnsubscriptionRequest extends QLMessage {

    @JsonProperty("instrument")
    private String instrument;

    public QLOrderBookUnsubscriptionRequest(String instrument) {
        this.instrument = instrument;
    }

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ORDER_BOOK_UNSUBSCRIPTION_REQUEST;
    }
}
