package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;

@JsonTypeName("Heartbeat")
@Getter
public final class QLHeartbeat extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.HEARTBEAT;
    }

    LocalTime time = LocalTime.MIN;
    Duration startTime = Duration.ZERO;
    Duration endTime = Duration.ofDays(1);
    Duration envStartTime = Duration.ZERO;
    Duration envEndTime = Duration.ofDays(1);
}
