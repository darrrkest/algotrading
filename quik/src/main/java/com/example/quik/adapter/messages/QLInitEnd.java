package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("InitEnd")
public final class QLInitEnd extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INIT_END;
    }
}
