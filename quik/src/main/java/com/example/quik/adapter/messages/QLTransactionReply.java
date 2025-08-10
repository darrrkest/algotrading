package com.example.quik.adapter.messages;

import com.example.quik.adapter.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonTypeName("TransactionReply")
@Builder
public class QLTransactionReply extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.TRANSACTION_REPLY;
    }

    @JsonProperty("trans_id")
    private long transId;

    @JsonProperty("status")
    private long status;

    @JsonProperty("result_msg")
    private String resultMsg;

    @JsonProperty("time")
    private long time;

    @JsonProperty("uid")
    private long uid;

    @JsonProperty("flags")
    private int flags;

    @JsonProperty("server_trans_id")
    private long serverTransId;

    @JsonProperty("order_num")
    private long orderNum;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("balance")
    private int balance;

    @JsonProperty("firm_id")
    private String firmId;

    @JsonProperty("account")
    private String account;

    @JsonProperty("client_code")
    private String clientCode;

    @JsonProperty("brokerref")
    private String brokerref;

    @JsonProperty("class_code")
    private String classCode;

    @JsonProperty("sec_code")
    private String secCode;

    public boolean isSuccessful() {
        return status == 0 || status == 1 || status == 3;
    }
}
