package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Информация о сессии
 */
@Getter
@SuperBuilder
public class SessionInfo extends ConnectorMessage {
    private @NotNull LocalTime serverTime;
    private @NotNull Duration startTime;
    private @NotNull Duration endTime;
    private @NotNull Duration eveningStartTime;
    private @NotNull Duration eveningEndTime;
}
