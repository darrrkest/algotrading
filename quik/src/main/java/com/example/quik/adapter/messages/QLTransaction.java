package com.example.quik.adapter.messages;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.quik.adapter.messages.transaction.*;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.Instrument;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

@Builder
@Getter
@Setter
@JsonTypeName("Transaction")
public final class QLTransaction extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.TRANSACTION;
    }

    @JsonProperty("ACTION")
    private QLOrderAction action;

    @JsonProperty("CLASSCODE")
    private String classCode;

    @JsonProperty("SECCODE")
    private String secCode;

    @JsonProperty("FIRM_ID")
    private String firmId;

    @JsonProperty("ACCOUNT")
    private String account;

    @JsonProperty("CLIENT_CODE")
    private String clientCode;

    @JsonProperty("TYPE")
    public QLOrderType type;

    @JsonProperty("OPERATION")
    public QLOrderOperation operation;

    @JsonProperty("EXECUTION_CONDITION")
    public QLExecutionCondition executionCondition;

    @JsonProperty("QUANTITY")
    private String quantity;

    @JsonProperty("PRICE")
    private String price;

    @JsonProperty("ORDER_KEY")
    private String orderKey;

    @JsonProperty("TRANS_ID")
    private String transId;

    @JsonProperty("COMMENT")
    private String comment;

    @JsonProperty("BASE_CONTRACT")
    private String baseContract;

    //region Stop/Take orders

    @JsonProperty("STOP_ORDER_KIND")
    public QLStopOrderKind stopOrderKind;

    @JsonProperty("STOPPRICE")
    private String stopPrice;

    @JsonProperty("STOPPRICE2")
    private String stopPrice2;

    @JsonProperty("LINKED_ORDER_PRICE")
    private String linkedOrderPrice;

    @JsonProperty("MARKET_STOP_LIMIT")
    public QLYesNo marketStopLimit;

    @JsonProperty("MARKET_TAKE_PROFIT")
    public QLYesNo marketTakeProfit;

    @JsonProperty("EXPIRY_DATE")
    private String expiryDate;

    @JsonProperty("OFFSET")
    private String offset;

    @JsonProperty("OFFSET_UNITS")
    public QLSpreadUnit offsetUnits;

    @JsonProperty("SPREAD")
    private String spread;

    @JsonProperty("SPREAD_UNITS")
    public QLSpreadUnit spreadUnits;

    @JsonProperty("BASE_ORDER_KEY")
    private String baseOrderKey;

    @JsonProperty("USE_BASE_ORDER_BALANCE")
    public QLYesNo useBaseOrderBalance;

    @JsonProperty("ACTIVATE_IF_BASE_ORDER_PARTLY_FILLED")
    public QLYesNo activateIfBaseOrderPartlyFilled;

    //endregion

    //region Modify transaction

    @JsonProperty("MODE")
    private String mode;

    @JsonProperty("FIRST_ORDER_NUMBER")
    private String firstOrderNumber;

    @JsonProperty("FIRST_ORDER_NEW_QUANTITY")
    private String firstOrderNewQuantity;

    @JsonProperty("FIRST_ORDER_NEW_PRICE")
    private String firstOrderNewPrice;

    //endregion

    //region Factory methods

    public static QLTransaction fromNewOrderTransaction(NewOrderTransaction transaction, long transId) {
        return builder()
                .action(QLOrderAction.NEW_ORDER)
                .account(transaction.getAccount())
                .clientCode(transaction.getComment())
                .secCode(transaction.getInstrument().getCode())
                .classCode(getClassCode(transaction.getInstrument()))
                .executionCondition(QLExecutionCondition.PUT_IN_QUEUE)
                .operation(transaction.getOperation() == OrderOperation.BUY ? QLOrderOperation.B : QLOrderOperation.S)
                .quantity(String.valueOf(transaction.getSize()))
                .price(new DecimalFormat("0.######").format(transaction.getPrice()))
                .transId(String.valueOf(transId))
                .type(QLOrderType.L)
                .expiryDate(QLOrderExpiryDate.fromDate(transaction.getGoodTill()))
                .build();
    }

    public static QLTransaction fromFillOrderTransaction(KillOrderTransaction transaction, long transId) {
        return builder()
                .action(QLOrderAction.KILL_ORDER)
                .account(transaction.getAccount())
                .clientCode(transaction.getAccount())
                .secCode(transaction.getInstrument().getCode())
                .classCode(getClassCode(transaction.getInstrument()))
                .executionCondition(QLExecutionCondition.PUT_IN_QUEUE)
                .transId(String.valueOf(transId))
                .orderKey(transaction.getOrderExchangeId())
                .build();
    }

    private static String getClassCode(Instrument instrument) {
        var classCode = "SPBFUT";

        // проверяем, не опцион ли, эвристика по коду инструмента
        if (instrument.getCode().length() > 5
                && instrument.getCode() != "USDRUBF" // вечные фьючерсы
                && instrument.getCode() != "EURRUBF"
                && instrument.getCode() != "CNYRUBF"
        ) {
            classCode = "SPBOPT";
        }

        return classCode;
    }

    //endregion
}
