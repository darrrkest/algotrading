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
    private @NotNull String account;

    /**
     * Информация об инструменте
     */
    private @NotNull Instrument instrument;

    /**
     * Операция
     */
    private @NotNull OrderOperation operation;

    /**
     * Цена заявки
     */
    private @NotNull BigDecimal price;

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
    private @NotNull OrderType type = OrderType.LIMIT;

    /**
     * Идентификатор заявки, присвоенный биржей
     */
    private @Nullable String orderExchangeId;

    /**
     * Идектификатор транзакции
     */
    private @NotNull UUID transactionId;

    /**
     * Состояние заявки
     */
    private @NotNull OrderState state;

    /**
     * Время жизни заявки
     */
    private @NotNull LocalDateTime goodTill;

    /**
     * Дата и время заявки
     */
    private @NotNull LocalDateTime dateTime;

    /**
     * Комментарий
     */
    private @NotNull String comment;

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
