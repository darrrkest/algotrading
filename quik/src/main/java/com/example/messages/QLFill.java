package com.example.messages;

import com.example.connector.messages.incoming.LiquidityIndicator;
import com.example.execution.OrderOperation;
import com.example.messages.transaction.QLFillFlags;
import com.example.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonTypeName("Fill")
public final class QLFill extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.FILL;
    }

    @JsonIgnore
    public OrderOperation getOperation() {
        return (flags & QLFillFlags.Sell) != 0 ? OrderOperation.SELL : OrderOperation.BUY;
    }

    @JsonProperty("trade_num")
    private long fillId;

    @JsonProperty("order_num")
    private long orderId;

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
    private int size;

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("client_code")
    private String clientCode;

    @JsonProperty("sec_code")
    private String secCode;

    @JsonProperty("class_code")
    private String classCode;

    @JsonProperty("datetime")
    private QLDateTime datetime;

    @JsonIgnore
    public LocalDateTime getTime() {
        return datetime.toLocalDateTime();
    }

    @JsonIgnore
    public LiquidityIndicator getLiquidityIndicator() {
        if ((flags & QLFillFlags.Active) != 0) return LiquidityIndicator.TAKER;
        if ((flags & QLFillFlags.Passive) != 0) return LiquidityIndicator.MAKER;
        return LiquidityIndicator.UNDEFINED;
    }
}
