package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

@Getter
@SuperBuilder
public abstract class InstrumentMessage extends ConnectorMessage {
    @NotNull
    public final Instrument instrument;
}
