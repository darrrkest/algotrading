package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("InitBegin")
public final class QLInitBegin extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INIT_BEGIN;
    }
}
