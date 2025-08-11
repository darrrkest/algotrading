package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.execution.StopOrder;
import com.example.abstractions.execution.StopOrderType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public final class NewStopOrderTransaction extends Transaction {
    /**
     * Тип стоп заявки
     */
    @NotNull
    private StopOrderType type;

    /**
     * Количество контрактов/лотов
     */
    private int size;

    /**
     * Операция стоп заявки
     */
    @NotNull
    private OrderOperation operation;

    /**
     * Цена, при которой срабатывает стоп заявка
     */
    @Nullable
    private Double stopLossPrice;

    /**
     * Проскальзывание. Как для СЛ, так и ТП
     */
    @Nullable
    private Double slippage;

    /**
     * Цена, при которой активируется тейк профит и начинается расчет
     */
    @Nullable
    private Double takeProfitPrice;

    /**
     * Отклонение от экстремума, при котором сработает тейк профит
     */
    @Nullable
    private Double takeProfitDeviation;

    /**
     * Биржевой ID заявки, при исполнении которой активируется сто заявка
     */
    @Nullable
    private String activatingOrderId;

    /**
     * Время жизни стоп заявки
     */
    @Nullable
    private LocalDateTime goodTill;

    /**
     * Комментарий к заявке
     */
    @Nullable
    private String comment;

    @NotNull
    public static NewStopOrderTransaction fromStopOrder(StopOrder order) {
        return builder()
                .account(order.getAccount())
                .instrument(order.getInstrument())
                .size(order.getSize())
                .operation(order.getOperation())
                .stopLossPrice(order.getStopLossPrice())
                .slippage(order.getSlippage())
                .takeProfitPrice(order.getTakeProfitTriggerPrice())
                .takeProfitDeviation(order.getTakeProfitDeviation())
                .activatingOrderId(order.getActivatingOrderId())
                .comment(order.getComment())
                .goodTill(order.getGoodTill())
                .transactionId(UUID.randomUUID())
                .build();
    }
}
