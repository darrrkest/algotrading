package com.example.symbology;

import lombok.Getter;

@Getter
public enum Venue {
    MOEX("MOEX"),
    CME("CME"),
    NYMEX("NYM");

    private final String name;

    Venue(String name) {
        this.name = name;
    }
}
