package com.example.abstractions.execution;

import com.example.abstractions.symbology.Instrument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public final class Order {
    // region required

    /**
     * Номер счета заявки
     */
    @NotNull
    private final String account;

    /**
     * Информация об инструменте
     */
    @NotNull
    private final Instrument instrument;

    /**
     * Операция
     */
    @NotNull
    private final OrderOperation operation;

    /**
     * Цена заявки
     */
    private final double price;

    /**
     * Количество контрактов/лотов
     */
    private final int size;

    // endregion

    // region final

    /**
     * Тип заявки
     */
    @NotNull
    @Builder.Default
    private final OrderType type = OrderType.LIMIT;

    /**
     * Идентификатор транзакции
     */
    @NotNull
    @Builder.Default
    private final UUID transactionId = UUID.randomUUID();

    /**
     * Время жизни заявки
     */
    @Nullable
    @Builder.Default
    private final LocalDateTime goodTill = null;

    /**
     * Дата и время заявки
     */
    @NotNull
    @Builder.Default
    private final LocalDateTime dateTime = LocalDateTime.now();

    /**
     * Комментарий
     */
    @NotNull
    @Builder.Default
    private final String comment = "";

    // endregion

    // region mutable

    /**
     * Активное(пока не исполнившееся) количество
     */
    @Setter
    private int activeSize;

    /**
     * Идентификатор заявки, присвоенный биржей
     */
    @Nullable
    @Setter
    @Builder.Default
    private String orderExchangeId = null;

    /**
     * Состояние заявки
     */
    @NotNull
    @Setter
    @Builder.Default
    private OrderState state = OrderState.UNDEFINED;

    // endregion

    @Override
    public String toString() {
        return "Order{" +
                "account='" + account + '\'' +
                ", instrument=" + instrument +
                ", operation=" + operation +
                ", price=" + price +
                ", size=" + size +
                ", activeSize=" + activeSize +
                ", type=" + type +
                ", OrderExchangeId='" + orderExchangeId + '\'' +
                ", transactionId=" + transactionId +
                ", state=" + state +
                '}';
    }
}
