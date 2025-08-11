package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.messages.TransactionMessageVisitor;
import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.execution.StopOrder;
import com.example.abstractions.execution.StopOrderType;
import com.example.abstractions.symbology.Instrument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@SuperBuilder
public final class NewStopOrderTransaction extends Transaction {

    // region required

    /**
     * Тип стоп заявки
     */
    @NotNull
    private final StopOrderType type;

    /**
     * Количество контрактов/лотов
     */
    private final int size;

    /**
     * Операция стоп заявки
     */
    @NotNull
    private final OrderOperation operation;

    // endregion

    // region final

    /**
     * Цена, при которой срабатывает стоп заявка
     */
    @Builder.Default
    private final double stopLossPrice;

    /**
     * Проскальзывание. Как для СЛ, так и ТП
     */
    @Builder.Default
    private final double slippage;

    /**
     * Цена, при которой активируется тейк профит и начинается расчет
     */
    @Builder.Default
    private final double takeProfitPrice;

    /**
     * Отклонение от экстремума, при котором сработает тейк профит
     */
    @Builder.Default
    private final double takeProfitDeviation;

    /**
     * Биржевой ID заявки, при исполнении которой активируется сто заявка
     */
    @Nullable
    @Builder.Default
    private final String activatingOrderId = null;

    /**
     * Время жизни стоп заявки
     */
    @Nullable
    @Builder.Default
    private final LocalDateTime goodTill = null;

    /**
     * Комментарий к заявке
     */
    @Nullable
    @Builder.Default
    private final String comment = null;

    // endregion

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

    @Override
    public void accept(TransactionMessageVisitor visitor) {
        visitor.visit(this);
    }
}
