package com.example.abstractions.execution;

import com.example.abstractions.symbology.Instrument;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Стоп заявка
 */
@Getter
@Builder
public final class StopOrder {

    /**
     * Номер счета заявки
     */
    @NotNull
    private String account;

    /**
     * Инструмент заявки
     */
    @NotNull
    private Instrument instrument;

    /**
     * Операция стоп заяки
     */
    @NotNull
    private OrderOperation operation;

    /**
     * Количество контрактов/лотов
     */
    private int size;

    /**
     * Тип стоп заявки
     */
    @NotNull
    private StopOrderType type;

    /**
     * Цена, по которой выставляется лимитная стоп-лосс заявка
     */
    private double stopLossPrice;

    /**
     * Цена, при которой срабатывает стоп заявка
     */
    private double stopLossTriggerPrice;

    /**
     * Проскальзывание. Как для СЛ, так и ТП
     */
    private double slippage;

    /**
     * Цена, при которой активируется тейк профит и начинается расчет
     */
    @Nullable
    private Double takeProfitTriggerPrice;

    /**
     * Отклонение от экстремума, при котором сработает тейк профит
     */
    private double takeProfitDeviation;

    /**
     * Биржевой ID заявки, при исполнении которой активируется стоп заявка
     */
    @Nullable
    private String activatingOrderId;

    /**
     * Биржевой идентификатор заявки
     */
    @Nullable
    private String orderExchangeId;

    /**
     * Идектификатор транзакции
     */
    @Nullable
    private UUID transactionId;

    /**
     * Состояние стоп заявки
     */
    @NotNull
    private StopOrderState state;

    /**
     * Время жизни стоп заявки
     */
    @Nullable
    private LocalDateTime goodTill;

    /**
     * Дата и время заявки
     */
    @Nullable
    private LocalDateTime dateTime;

    /**
     * Комментарий к заявке
     */
    @Nullable
    private String comment;
}
