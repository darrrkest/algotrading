package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "ql")
@Component
public class QLConnectorConfig {
    private List<ConnectionConfig> connections;

    @Getter
    @Setter
    public static class ConnectionConfig {
        private String host;
        private int port;
        private String name;
    }

}
