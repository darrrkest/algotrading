package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.TransactionVisitor;
import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public abstract class Transaction extends ConnectorMessage {
    UUID transactionId;

    String account;

    Instrument instrument;

    abstract void Visit(TransactionVisitor visitor);
}
