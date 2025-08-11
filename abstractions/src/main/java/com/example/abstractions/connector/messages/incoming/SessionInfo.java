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
    private final LocalTime serverTime;

    @Nullable
    private final Duration startTime;

    @Nullable
    private final Duration endTime;

    @Nullable
    private final Duration eveningStartTime;

    @Nullable
    private final Duration eveningEndTime;
}
