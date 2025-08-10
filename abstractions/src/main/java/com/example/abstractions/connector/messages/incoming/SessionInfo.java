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
public final class SessionInfo extends ConnectorMessage {
    @NotNull
    private LocalTime serverTime;

    @NotNull
    private Duration startTime;

    @NotNull
    private Duration endTime;

    @NotNull
    private Duration eveningStartTime;

    @NotNull
    private Duration eveningEndTime;
}
