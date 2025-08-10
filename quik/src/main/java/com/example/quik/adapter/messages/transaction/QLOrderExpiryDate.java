package com.example.quik.adapter.messages.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class QLOrderExpiryDate {
    public static final String GTC = "GTC";
    public static final String TODAY = "TODAY";

    public static String fromDate(LocalDateTime date) {
        if (date == null) return TODAY;

        if (date == LocalDateTime.MAX) return GTC;

        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
