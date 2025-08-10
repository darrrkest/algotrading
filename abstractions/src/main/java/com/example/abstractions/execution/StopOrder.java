package com.example.abstractions.execution;

import com.example.abstractions.symbology.Instrument;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
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
     * Цена, по которой выставляется лимитная стоп лосс заявка
     */
    @Nullable
    private BigDecimal stopLossPrice;

    /**
     * Цена, при которой срабатывает стоп заявка
     */
    @Nullable
    private BigDecimal stopLossTriggerPrice;

    /**
     * Проскальзывание. Как для СЛ, так и ТП
     */
    @Nullable
    private BigDecimal slippage;

    /**
     * Цена, при которой активируется тейк профит и начинается расчет
     */
    @Nullable
    private BigDecimal takeProfitTriggerPrice;

    /**
     * Отконение от экстремума, при котором сработает тейкк профит
     */
    @Nullable
    private BigDecimal takeProfitDeviation;

    /**
     * Биржевой ID заявки, при исполнении которой активируется сто заявка
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
