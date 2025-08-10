package com.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class InstrumentDefinition {
    @JsonProperty("Root")
    private String root;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("CodeTemplate")
    private String codeTemplate;

    @JsonProperty("Exchange")
    private String exchange;

    @JsonProperty("Expirations")
    private List<String> expirations;
}

