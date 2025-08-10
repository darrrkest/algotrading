package com.example.abstractions.execution;

import com.example.abstractions.symbology.Instrument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public final class Order {
    /**
     * Номер счета заявки
     */
    @NotNull
    private String account;

    /**
     * Информация об инструменте
     */
    @NotNull
    private Instrument instrument;

    /**
     * Операция
     */
    @NotNull
    private OrderOperation operation;

    /**
     * Цена заявки
     */
    @NotNull
    private BigDecimal price;

    /**
     * Количество контрактов/лотов
     */
    private int size;

    /**
     * Активное(пока не исполнившееся) количество
     */
    private int activeSize;

    /**
     * Тип заявки
     */
    @NotNull
    private OrderType type = OrderType.LIMIT;

    /**
     * Идентификатор заявки, присвоенный биржей
     */
    @Nullable
    private String orderExchangeId;

    /**
     * Идектификатор транзакции
     */
    @NotNull
    private UUID transactionId;

    /**
     * Состояние заявки
     */
    @NotNull
    private OrderState state;

    /**
     * Время жизни заявки
     */
    @NotNull
    private LocalDateTime goodTill;

    /**
     * Дата и время заявки
     */
    @NotNull
    private LocalDateTime dateTime;

    /**
     * Комментарий
     */
    @NotNull
    private String comment;

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
