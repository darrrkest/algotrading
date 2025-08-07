package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("InitBegin")
public final class QLInitBegin extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INIT_BEGIN;
    }
}
