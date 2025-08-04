package com.example.connector.messages.incoming;

import com.example.connector.ConnectorMessageVisitor;
import com.example.connector.messages.ConnectorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class TransactionReply extends ConnectorMessage {

    @JsonProperty("TransactionId")
    private UUID transactionId;

    @JsonProperty("Success")
    boolean success;

    @JsonProperty("Message")
    String message;
}
