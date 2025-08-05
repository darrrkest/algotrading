package com.example.abstractions.symbology;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Instrument {
    String root;
    String code;
    String exchange;
    String description;
    InstrumentType type;
    LocalDate expiration;

    public Instrument(String code) {
        this.code = code;
    }

    public Instrument(String root, InstrumentType type, String description,
                      String exchange, LocalDate expiration, String code) {
        this.root = root;
        this.type = type;
        this.description = description;
        this.exchange = exchange;
        this.expiration = expiration;
        this.code = code;
    }
}

