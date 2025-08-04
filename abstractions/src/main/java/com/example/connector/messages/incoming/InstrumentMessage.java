package com.example.connector.messages.incoming;

import com.example.connector.messages.ConnectorMessage;
import com.example.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class InstrumentMessage extends ConnectorMessage {
    public Instrument instrument;
}
