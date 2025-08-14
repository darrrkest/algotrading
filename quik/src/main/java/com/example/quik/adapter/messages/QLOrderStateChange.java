package com.example.quik.adapter.messages;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.execution.OrderState;
import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.example.quik.adapter.messages.transaction.QLOrderFlags;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonTypeName("OrderStateChange")
public final class QLOrderStateChange extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ORDER_STATE_CHANGE;
    }

    @JsonProperty("order_num")
    private long orderExchangeId;

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("brokerref")
    private String brokerRef;

    @JsonProperty("userid")
    private String userId;

    @JsonProperty("firmid")
    private String firmId;

    @JsonProperty("account")
    private String account;

    @JsonProperty("price")
    private double price;

    @JsonProperty("qty")
    private int quantity;

    @JsonProperty("balance")
    private int balance;

    @JsonProperty("filled")
    private int filled;

    @JsonProperty("trans_id")
    private long transId;

    @JsonProperty("client_code")
    private String clientCode;

    @JsonProperty("exchange_code")
    private String exchangeCode;

    @JsonProperty("activation_time")
    private long activationTime;

    @JsonProperty("sec_code")
    private String secCode;

    @JsonProperty("class_code")
    private String classCode;

    @JsonProperty("linkedorder")
    private long originalOrderExchangeId;

    @JsonProperty("datetime")
    private QLDateTime datetime;

    @JsonProperty("withdraw_datetime")
    private QLDateTime withdrawDatetime;

    @JsonProperty("value_entry_type")
    private byte valueEntryType;

    @JsonProperty("reject_reason")
    private String rejectReason;

    @JsonProperty("ext_order_status")
    private int extOrderStatus;

    @JsonProperty("filled_value")
    private double filledValue;

    @JsonProperty("extref")
    private String extRef;

    @JsonIgnore
    public LocalDateTime getTime() {
        return (getState() != OrderState.CANCELLED ? datetime : withdrawDatetime).toLocalDateTime();
    }

    @JsonIgnore
    public OrderState getState() {
        boolean isActive = (flags & QLOrderFlags.ACTIVE.getValue()) != 0;
        boolean isCancelled = (flags & QLOrderFlags.CANCELLED.getValue()) != 0;
        if (isActive) return balance == quantity ? OrderState.ACTIVE : OrderState.PARTIALLY_FILLED;
        return isCancelled ? OrderState.CANCELLED : OrderState.FILLED;
    }

    @JsonIgnore
    public OrderOperation getOperation() {
        return (flags & QLOrderFlags.SELL.getValue()) != 0 ? OrderOperation.SELL : OrderOperation.BUY;
    }
}
