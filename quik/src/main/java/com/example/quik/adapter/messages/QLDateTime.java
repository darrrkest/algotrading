package com.example.quik.adapter.messages;

import java.time.LocalDateTime;

public record QLDateTime(int year, int month, int day, int week_day, int hour, int min, int sec, int ms, int mcs) {
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, day, hour, min, sec);
    }
}
