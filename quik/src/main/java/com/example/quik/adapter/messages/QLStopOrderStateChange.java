package com.example.quik.adapter.messages;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.execution.OrderState;
import com.example.abstractions.execution.StopOrderState;
import com.example.quik.adapter.messages.transaction.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonTypeName("StopOrderStateChange")
public class QLStopOrderStateChange extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.STOP_ORDER_STATE_CHANGE;
    }

    @JsonProperty("order_num")
    private long orderExchangeId;

    @JsonProperty("ordertime")
    private long orderTime;

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("brokerref")
    private String brokerRef;

    @JsonProperty("firmid")
    private String firmId;

    @JsonProperty("account")
    private String account;

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("condition_price")
    private double conditionPrice;

    @JsonProperty("price")
    private double price;

    @JsonProperty("qty")
    private int quantity;

    @JsonProperty("linkedorder")
    private int resultOrderExchangeId;

    @JsonProperty("expiry")
    private long expiry;

    @JsonProperty("trans_id")
    private long transId;

    @JsonProperty("client_code")
    private String clientCode;

    @JsonProperty("co_order_num")
    private long coOrderNum;

    @JsonProperty("co_order_price")
    private double coOrderPrice;

    @JsonProperty("stop_order_type")
    private QLStopOrderKind stopOrderType;

    @JsonProperty("orderdate")
    public long orderDate;

    @JsonProperty("alltrade_num")
    public long allTradeNum;

    @JsonProperty("stopflags")
    public int stopFlags;

    @JsonProperty("offset")
    public double offset;

    @JsonProperty("spread")
    public double spread;

    @JsonProperty("balance")
    public int balance;

    @JsonProperty("uid")
    public String UID;

    @JsonProperty("filled_qty")
    public int filledSize;

    @JsonProperty("withdraw_time")
    public long withdrawTime;

    @JsonProperty("condition_price2")
    public double conditionPrice2;

    @JsonProperty("active_from_time")
    public long activeFromTime;

    @JsonProperty("active_to_time")
    public long activeToTime;

    @JsonProperty("sec_code")
    public String secCode;

    @JsonProperty("class_code")
    public String classCode;

    @JsonProperty("condition_sec_code")
    public String conditionSecCode;

    @JsonProperty("condition_class_code")
    public String conditionClassCode;

    @JsonProperty("canceled_uid")
    public int canceledUID;

    @JsonProperty("order_date_time")
    public QLDateTime orderDateTime;

    @JsonProperty("withdraw_datetime")
    public QLDateTime withdrawDatetime;

    @JsonProperty("activation_date_time")
    public QLDateTime activationDateTime;

    @JsonIgnore
    public StopOrderState getState() {
        if ((flags & QLStopOrderFlags.WAITING_FOR_ACTIVATION.getValue()) != 0) {
            return StopOrderState.WAITING_FOR_ACTIVATION;
        }

        if ((flags & QLStopOrderFlags.ACTIVATED_REJECTED_LIMITS.getValue()) != 0) {
            return StopOrderState.ERROR;
        }

        if ((flags & QLStopOrderFlags.ACTIVATED_REJECTED_BY_EXCHANGE.getValue()) != 0) {
            return StopOrderState.ERROR;
        }

        if ((flags & QLStopOrderFlags.MIN_MAX_IS_CALCULATING.getValue()) != 0) {
            return StopOrderState.TRAILING_TAKE_PROFIT;
        }

        if ((flags & QLStopOrderFlags.CANCELLED.getValue()) != 0) {
            return StopOrderState.CANCELLED;
        }

        if ((flags & QLStopOrderFlags.ACTIVE.getValue()) != 0) {
            return StopOrderState.ACTIVE;
        }

        return StopOrderState.FILLED;
    }

    @JsonIgnore
    public OrderOperation getOperation() {
        return (flags & QLOrderFlags.SELL.getValue()) != 0 ? OrderOperation.SELL : OrderOperation.BUY;
    }
}
