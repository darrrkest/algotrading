package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("InitEnd")
public final class QLInitEnd extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INIT_END;
    }
}
