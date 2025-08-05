package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonTypeName("OrderBook")
public class QLOrderBook extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ORDER_BOOK;
    }

    @JsonProperty("instrument")
    private String instrument;

    @JsonProperty("offers")
    private List<QLQuote> offers;

    @JsonProperty("bids")
    private List<QLQuote> bids;

    @JsonIgnore
    public boolean hasOffers() {
        return offers != null && !offers.isEmpty();
    }

    @JsonIgnore
    public boolean hasBids() {
        return bids != null && !bids.isEmpty();
    }
}
