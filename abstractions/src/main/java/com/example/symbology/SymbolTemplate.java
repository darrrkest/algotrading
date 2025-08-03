package com.example.symbology;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Map.entry;

public class SymbolTemplate {

    private final String template;

    public SymbolTemplate(String template) {
        this.template = template;
    }

    public String render(String root, String exchange, LocalDate expiration) {
        String result = template
                .replace("{root}", root)
                .replace("{exchange}", exchange);

        if (expiration != null) {
            result = result
                    .replace("{month:F}", getFuturesMonthCode(expiration))
                    .replace("{year:1}", String.valueOf(expiration.getYear() % 10))
                    .replace("{year:2}", expiration.format(DateTimeFormatter.ofPattern("yy")));
        }

        return result;
    }

    private String getFuturesMonthCode(LocalDate expiration) {
        Map<Integer, String> monthCodes = Map.ofEntries(
                entry(1, "F"),
                entry(2, "G"),
                entry(3, "H"),
                entry(4, "J"),
                entry(5, "K"),
                entry(6, "M"),
                entry(7, "N"),
                entry(8, "Q"),
                entry(9, "U"),
                entry(10, "V"),
                entry(11, "X"),
                entry(12, "Z")
        );
        String code = monthCodes.get(expiration.getMonthValue());
        if (code == null) {
            throw new IllegalArgumentException("Invalid month for futures: " + expiration);
        }
        return code;
    }
}
