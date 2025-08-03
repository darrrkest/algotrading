package com.example.connector.messages.outgoing;

import com.example.connector.TransactionVisitor;
import com.example.connector.messages.ConnectorMessage;
import com.example.symbology.Instrument;
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
