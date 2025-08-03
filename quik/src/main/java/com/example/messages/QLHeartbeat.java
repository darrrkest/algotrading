package com.example.messages;

import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.Duration;

@JsonTypeName("Heartbeat")
public final class QLHeartbeat extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.HEARTBEAT;
    }

    Duration startTime = Duration.ZERO;
    Duration endTime = Duration.ofDays(1);
    Duration envStartTime = Duration.ZERO;
    Duration envEndTime = Duration.ofDays(1);
}
