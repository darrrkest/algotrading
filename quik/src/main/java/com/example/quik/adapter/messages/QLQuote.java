package com.example.quik.adapter.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public final class QLQuote {

    @JsonProperty("p")
    private BigDecimal price;

    @JsonProperty("q")
    private int size;
}
