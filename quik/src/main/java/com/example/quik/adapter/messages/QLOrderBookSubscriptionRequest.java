package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonTypeName("OrderBookSubscriptionRequest")
public class QLOrderBookSubscriptionRequest extends QLMessage {

    @JsonProperty("instrument")
    private String instrument;

    @JsonProperty("id")
    private UUID id;

    public QLOrderBookSubscriptionRequest(String instrument) {
        this.instrument = instrument;
        id = UUID.randomUUID();
    }

    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ORDER_BOOK_SUBSCRIPTION_REQUEST;
    }
}
