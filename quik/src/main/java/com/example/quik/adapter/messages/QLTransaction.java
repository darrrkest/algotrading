package com.example.quik.adapter.messages;

import com.example.abstractions.connector.messages.outgoing.KillOrderTransaction;
import com.example.abstractions.connector.messages.outgoing.NewStopOrderTransaction;
import com.example.quik.adapter.messages.transaction.*;
import com.example.abstractions.connector.messages.outgoing.NewOrderTransaction;
import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.Instrument;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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
                .price(toDecimalFormat(transaction.getPrice()))
                .transId(String.valueOf(transId))
                .type(QLOrderType.L)
                .expiryDate(QLOrderExpiryDate.fromDate(transaction.getGoodTill()))
                .build();
    }

    public static QLTransaction fromKillOrderTransaction(KillOrderTransaction transaction, long transId) {
        return builder()
                .action(QLOrderAction.KILL_ORDER)
                .account(transaction.getAccount())
                .clientCode(transaction.getAccount()) // TODO проверить
                .secCode(transaction.getInstrument().getCode())
                .classCode(getClassCode(transaction.getInstrument()))
                .executionCondition(QLExecutionCondition.PUT_IN_QUEUE)
                .transId(String.valueOf(transId))
                .orderKey(transaction.getOrderExchangeId())
                .build();
    }

    public static QLTransaction fromNewStopOrderTransaction(NewStopOrderTransaction transaction, long transId) {
        var kind = switch (transaction.getType()) {
            case STOP_LOSS -> QLStopOrderKind.SIMPLE_STOP_ORDER;
            case TAKE_PROFIT -> QLStopOrderKind.TAKE_PROFIT_STOP_ORDER;
            case TAKE_PROFIT_AND_STOP_LOSS -> QLStopOrderKind.TAKE_PROFIT_AND_STOP_LIMIT_ORDER;
            case STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER -> QLStopOrderKind.ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER;
            case TAKE_PROFIT_ACTIVATED_BY_LIMIT_ORDER -> QLStopOrderKind.ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER;
            case TAKE_PROFIT_AND_STOP_LOSS_ACTIVATED_BY_LIMIT_ORDER ->
                    QLStopOrderKind.ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER;
        };

        var stopPrice = switch (kind) {
            case TAKE_PROFIT_STOP_ORDER,
                 TAKE_PROFIT_AND_STOP_LIMIT_ORDER,
                 ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_STOP_ORDER -> transaction.getTakeProfitPrice();
            default -> transaction.getStopLossPrice();
        };

        var stopPrice2 = switch (kind) {
            case TAKE_PROFIT_AND_STOP_LIMIT_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER -> transaction.getStopLossPrice();
            default -> null;
        };

        var price = switch (kind) {
            case SIMPLE_STOP_ORDER,
                 ACTIVATED_BY_ORDER_SIMPLE_STOP_ORDER,
                 ACTIVATED_BY_ORDER_TAKE_PROFIT_AND_STOP_LIMIT_ORDER ->
                    transaction.getStopLossPrice() + transaction.getSlippage() * (transaction.getOperation() == OrderOperation.BUY ? 1 : -1);
            default -> transaction.getStopLossPrice();
        };

        return builder()
                .action(QLOrderAction.NEW_STOP_ORDER)
                .account(transaction.getAccount())
                .clientCode(transaction.getComment())  // TODO проверить
                .secCode(transaction.getInstrument().getCode())
                .classCode(getClassCode(transaction.getInstrument()))
                .operation(transaction.getOperation() == OrderOperation.BUY ? QLOrderOperation.B : QLOrderOperation.S)
                .quantity(String.valueOf(transaction.getSize()))
                .quantity(String.valueOf(transId))
                .stopOrderKind(kind)
                .price(toDecimalFormat(price))
                .stopPrice(toDecimalFormat(stopPrice))
                .stopPrice2(toDecimalFormat(stopPrice2))
                .offset(toDecimalFormat(transaction.getTakeProfitDeviation()))
                .offsetUnits(QLSpreadUnit.PRICE_UNITS)
                .spread(toDecimalFormat(transaction.getSlippage()))
                .spreadUnits(QLSpreadUnit.PRICE_UNITS)
                .baseOrderKey(transaction.getActivatingOrderId())
                .useBaseOrderBalance(QLYesNo.YES)
                .activateIfBaseOrderPartlyFilled(QLYesNo.YES)
                .expiryDate(QLOrderExpiryDate.fromDate(transaction.getGoodTill()))
                .build();
    }

    private static String toDecimalFormat(@Nullable Double value) {
        return new DecimalFormat("0.######").format(value);
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
