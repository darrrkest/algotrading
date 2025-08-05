package com.example.quik.messages.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QLOrderExpiryDate {
    public static final String GTC = "GTC";
    public static final String TODAY = "TODAY";

    public static String fromDate(LocalDateTime date) {
        if (date == null) return TODAY;

        if (date == LocalDateTime.MAX) return GTC;

        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
