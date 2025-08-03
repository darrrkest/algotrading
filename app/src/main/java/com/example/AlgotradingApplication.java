package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlgotradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlgotradingApplication.class, args);
        var t = new QLAdapterImpl("localhost", 1248);
        t.start();
    }
}