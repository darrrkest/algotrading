package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "message_type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QLHeartbeat.class, name = "Heartbeat"),
        @JsonSubTypes.Type(value = QLAccountsList.class, name = "AccountsList"),
        @JsonSubTypes.Type(value = QLEnvelopeAcknowledgment.class, name = "EnvAck"),
        @JsonSubTypes.Type(value = QLInitBegin.class, name = "InitBegin"),
        @JsonSubTypes.Type(value = QLInitEnd.class, name = "InitEnd"),
        @JsonSubTypes.Type(value = QLInstrumentParams.class, name = "InstrumentParams"),
        @JsonSubTypes.Type(value = QLInstrumentParamsSubscriptionRequest.class, name = "InstrumentParamsSubscriptionRequest"),
        @JsonSubTypes.Type(value = QLInstrumentParamsUnsubscriptionRequest.class, name = "InstrumentParamsUnsubscriptionRequest"),
        @JsonSubTypes.Type(value = QLTransaction.class, name = "Transaction"),
        @JsonSubTypes.Type(value = QLInstrumentsList.class, name = "InstrumentsList"),
        @JsonSubTypes.Type(value = QLMoneyPosition.class, name = "MoneyPosition"),
        @JsonSubTypes.Type(value = QLPosition.class, name = "Position"),
        @JsonSubTypes.Type(value = QLOrderStateChange.class, name = "OrderStateChange"),
        @JsonSubTypes.Type(value = QLFill.class, name = "Fill"),
        @JsonSubTypes.Type(value = QLTransactionReply.class, name = "TransactionReply"),
        @JsonSubTypes.Type(value = QLOrderBook.class, name = "OrderBook"),
        @JsonSubTypes.Type(value = QLOrderBookSubscriptionRequest.class, name = "OrderBookSubscriptionRequest"),
        @JsonSubTypes.Type(value = QLOrderBookUnsubscriptionRequest.class, name = "OrderBookUnsubscriptionRequest"),
})
public abstract class QLMessage {
    @JsonIgnore
    public abstract QLMessageType getMessageType();
}
