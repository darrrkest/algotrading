package com.example.quik.adapter.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class QLQuote {

    @JsonProperty("p")
    private double price;

    @JsonProperty("q")
    private int size;
}
