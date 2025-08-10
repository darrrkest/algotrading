package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Сообщение-транзакция
 */
@Getter
@SuperBuilder
public abstract class Transaction extends ConnectorMessage {
    protected @NotNull UUID transactionId;

    protected @NotNull String account;

    protected @NotNull Instrument instrument;

    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }
}
