package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Сообщение-ответ на транзакцию
 */
@Getter
@SuperBuilder
public final class TransactionReply extends ConnectorMessage {

    @JsonProperty("TransactionId")
    @NotNull
    private final UUID transactionId;

    @JsonProperty("Success")
    private final boolean success;

    @JsonProperty("Message")
    @Nullable
    private final String message;

    public static TransactionReply accepted(UUID transactionId) {
        return builder()
                .transactionId(transactionId)
                .success(true)
                .build();
    }

    public static TransactionReply accepted(Transaction transaction) {
        return accepted(transaction.getTransactionId());
    }

    public static TransactionReply rejected(UUID transactionId, String message) {
        return builder()
                .transactionId(transactionId)
                .message(message)
                .success(false)
                .build();
    }

    public static TransactionReply rejected(Transaction transaction, String message) {
        return rejected(transaction.getTransactionId(), message);
    }
}
