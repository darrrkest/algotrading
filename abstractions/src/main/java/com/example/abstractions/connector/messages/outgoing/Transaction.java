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
public abstract sealed class Transaction extends ConnectorMessage permits KillOrderTransaction, ModifyOrderTransaction, NewOrderTransaction, NewStopOrderTransaction {
    @NotNull
    protected final UUID transactionId;

    @NotNull
    protected final String account;

    @NotNull
    protected final Instrument instrument;

    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }
}
