package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private Duration startTime;

    @Nullable
    private Duration endTime;

    @Nullable
    private Duration eveningStartTime;

    @Nullable
    private Duration eveningEndTime;
}
