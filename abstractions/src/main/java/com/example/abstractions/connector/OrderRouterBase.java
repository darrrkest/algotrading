package com.example.abstractions.connector;

import com.example.abstractions.connector.events.AccountAddedEvent;
import com.example.abstractions.connector.messages.incoming.FillMessage;
import com.example.abstractions.connector.messages.incoming.MoneyPosition;
import com.example.abstractions.connector.messages.incoming.PositionMessage;
import com.example.abstractions.connector.messages.outgoing.Transaction;
import com.example.abstractions.symbology.Instrument;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Базовый класс типичного роутера
 */
public abstract class OrderRouterBase extends ConnectorService implements OrderRouter  {
    private static final int SESSION_UID_LENGTH = 2;
    private static final int COMMENT_LENGTH = 5;

    private final boolean storeFillsInMemory;
    private final Set<String> mapPermittedAccounts;
    private final Set<String> availableAccounts = ConcurrentHashMap.newKeySet();
    private final Object syncRoot = new Object();

    @Getter
    private final Map<String, Map<Instrument, List<FillMessage>>> fills = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, Map<Instrument, PositionMessage>> portfolios = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, MoneyPosition> limits = new ConcurrentHashMap<>();

    @Getter
    private final String sessionUid;

    protected OrderRouterBase(ApplicationEventPublisher eventPublisher, boolean storeFillsInMemory,
                          Collection<String> permittedAccounts,
                          String sessionUid) {
        super(eventPublisher);
        this.storeFillsInMemory = storeFillsInMemory;
        this.sessionUid = (sessionUid != null ? sessionUid : UUID.randomUUID().toString()).substring(0, SESSION_UID_LENGTH);
        this.mapPermittedAccounts = new HashSet<>();
        if (permittedAccounts != null) {
            this.mapPermittedAccounts.addAll(permittedAccounts);
        }
    }

    protected OrderRouterBase(ApplicationEventPublisher eventPublisher) {
        this(eventPublisher, true, null, null);
    }

    @Override
    public @NotNull List<String> getAvailableAccounts() {
        synchronized (syncRoot) {
            return new ArrayList<>(availableAccounts);
        }
    }

    /**
     * Публикует {@link AccountAddedEvent}
     * @param account добавленный счет
     */
    private void raiseAccountAdded(@NotNull String account) {
        eventPublisher.publishEvent(new AccountAddedEvent(this, account));
    }

    /**
     * Добавление счета в список доступных
     * @param account строковое представление счета
     */
    protected void addAccount(String account) {
        if (account == null || account.isBlank())
            return;

        boolean added;
        synchronized (syncRoot) {
            added = availableAccounts.add(account);
            if (added) {
                fills.put(account, new ConcurrentHashMap<>());
            }
        }

        if (added) {
            raiseAccountAdded(account);
        }
    }

    /**
     * Сохраняют свою сделку
     * @param fill сделка
     */
    protected void addFill(FillMessage fill) {
        if (!storeFillsInMemory)
            return;

        String account = fill.getAccount();
        AtomicBoolean gotNewAccount = new AtomicBoolean(false);

        synchronized (syncRoot) {
            Map<Instrument, List<FillMessage>> accountFills = fills.computeIfAbsent(account, k -> {
                gotNewAccount.set(true);
                availableAccounts.add(account);
                return new ConcurrentHashMap<>();
            });

            List<FillMessage> instrumentFills = accountFills.computeIfAbsent(fill.getInstrument(), k -> new ArrayList<>());
            instrumentFills.add(fill);
        }

        if (gotNewAccount.get()) {
            raiseAccountAdded(account);
        }
    }

    @Override
    public void sendTransaction(@NotNull Transaction transaction) {
        sendTransactionImpl(transaction);
    }

    /**
     * Отправка транзакции на биржу
     * @param transaction экземпляр транзакция для отправки
     */
    protected abstract void sendTransactionImpl(Transaction transaction);
}
