package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
public class PositionMessage extends AccountMessage {
    private String account;
    private ConnectorType connectorType;
    private Instrument instrument;
    private int quantity;
    private BigDecimal price;
}
