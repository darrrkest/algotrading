package com.example.abstractions.execution;

import com.example.abstractions.symbology.Instrument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    // region required

    /**
     * Номер счета заявки
     */
    @NotNull
    private final String account;

    /**
     * Инструмент заявки
     */
    @NotNull
    private final Instrument instrument;

    /**
     * Операция стоп заяки
     */
    @NotNull
    private final OrderOperation operation;

    /**
     * Количество контрактов/лотов
     */
    private final int size;

    /**
     * Тип стоп заявки
     */
    @NotNull
    private final StopOrderType type;

    // endregion

    // region final

    /**
     * Цена, по которой выставляется лимитная стоп-лосс заявка
     */
    private final double stopLossPrice;

    /**
     * Цена, при которой срабатывает стоп заявка
     */
    private final double stopLossTriggerPrice;

    /**
     * Проскальзывание. Как для СЛ, так и ТП
     */
    private final double slippage;

    /**
     * Цена, при которой активируется тейк профит и начинается расчет
     */
    @Nullable
    @Builder.Default
    private final Double takeProfitTriggerPrice = null;

    /**
     * Отклонение от экстремума, при котором сработает тейк профит
     */
    private final double takeProfitDeviation;

    /**
     * Биржевой ID заявки, при исполнении которой активируется стоп заявка
     */
    @Nullable
    @Builder.Default
    private final String activatingOrderId = null;

    /**
     * Идектификатор транзакции
     */
    @Nullable
    @Builder.Default
    private final UUID transactionId = UUID.randomUUID();

    /**
     * Время жизни стоп заявки
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
     * Комментарий к заявке
     */
    @Nullable
    @Builder.Default
    private final String comment = null;

    // endregion

    // region mutable

    /**
     * Состояние стоп заявки
     */
    @NotNull
    @Setter
    @Builder.Default
    private StopOrderState state = StopOrderState.UNDEFINED;

    /**
     * Биржевой идентификатор заявки
     */
    @Nullable
    @Setter
    private String orderExchangeId;

    // endregion
}
