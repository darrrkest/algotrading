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
@Setter
public final class Order {
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

    /**
     * Активное(пока не исполнившееся) количество
     */
    @Setter
    private int activeSize;

    /**
     * Тип заявки
     */
    @NotNull
    @Setter
    private OrderType type = OrderType.LIMIT;

    /**
     * Идентификатор заявки, присвоенный биржей
     */
    @Nullable
    @Setter
    private String orderExchangeId;

    /**
     * Идентификатор транзакции
     */
    @NotNull
    @Setter
    private UUID transactionId;

    /**
     * Состояние заявки
     */
    @NotNull
    @Setter
    private OrderState state;

    /**
     * Время жизни заявки
     */
    @Nullable
    @Setter
    private LocalDateTime goodTill = null;

    /**
     * Дата и время заявки
     */
    @NotNull
    @Setter
    private LocalDateTime dateTime = LocalDateTime.now();

    /**
     * Комментарий
     */
    @NotNull
    @Setter
    private String comment;

    public Order(@NotNull String account,
                 @NotNull Instrument instrument,
                 @NotNull OrderOperation operation,
                 double price,
                 int size) {
        this.account = account;
        this.instrument = instrument;
        this.operation = operation;
        this.price = price;
        this.size = size;
    }

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
