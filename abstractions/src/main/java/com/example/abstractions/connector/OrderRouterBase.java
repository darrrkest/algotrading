/*
package com.example.abstractions.connector;

import com.example.abstractions.symbology.Instrument;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class OrderRouterBase implements OrderRouter {
    private static final int SESSION_UID_LENGTH = 2;
    private static final int COMMENT_LENGTH = 5;

    private final boolean storeFillsInMemory;
    private final Set<String> mapPermittedAccounts;
    private final Set<String> availableAccounts = ConcurrentHashMap.newKeySet();
    private final Object syncRoot = new Object();

    private final Map<String, Map<Instrument, List<FillMessage>>> fills = new ConcurrentHashMap<>();
    private final Map<String, Map<Instrument, PositionMessage>> portfolios = new ConcurrentHashMap<>();
    private final Map<String, MoneyPosition> limits = new ConcurrentHashMap<>();

    private final List<Consumer<String>> accountAddedListeners = new CopyOnWriteArrayList<>();

    private final String sessionUid;
    private final OrderRouterMode mode;

    protected OrderRouter(boolean storeFillsInMemory,
                          Collection<String> permittedAccounts,
                          String sessionUid,
                          OrderRouterMode mode) {
        this.storeFillsInMemory = storeFillsInMemory;
        this.mode = mode;
        this.sessionUid = (sessionUid != null ? sessionUid : UUID.randomUUID().toString()).substring(0, SESSION_UID_LENGTH);
        this.mapPermittedAccounts = new HashSet<>();
        if (permittedAccounts != null) {
            this.mapPermittedAccounts.addAll(permittedAccounts);
        }
    }

    protected OrderRouter() {
        this(true, null, null, OrderRouterMode.THIS_SESSION_ONLY);
    }

    public String getSessionUid() {
        return sessionUid;
    }

    public OrderRouterMode getMode() {
        return mode;
    }

    public boolean isReceiveExternalOrders() {
        return mode != OrderRouterMode.THIS_SESSION_ONLY;
    }

    public boolean isCheckComment() {
        return mode != OrderRouterMode.EXTERNAL_SESSIONS_RENEWABLE;
    }

    public List<String> getAvailableAccounts() {
        synchronized (syncRoot) {
            return new ArrayList<>(availableAccounts);
        }
    }

    public Map<String, Map<IInstrumentTreeNode, List<FillMessage>>> getFills() {
        return fills;
    }

    public Map<String, Map<IInstrumentTreeNode, PositionMessage>> getPortfolios() {
        return portfolios;
    }

    public Map<String, MoneyPosition> getLimits() {
        return limits;
    }

    public void addAccountAddedListener(Consumer<String> listener) {
        accountAddedListeners.add(listener);
    }

    protected void raiseAccountAdded(String account) {
        for (var listener : accountAddedListeners) {
            listener.accept(account);
        }
    }

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

    protected void addFill(FillMessage fill) {
        if (!storeFillsInMemory)
            return;

        String account = fill.getAccount();
        boolean gotNewAccount = false;

        synchronized (syncRoot) {
            Map<IInstrumentTreeNode, List<FillMessage>> accountFills = fills.computeIfAbsent(account, k -> {
                gotNewAccount = true;
                availableAccounts.add(account);
                return new ConcurrentHashMap<>();
            });

            List<FillMessage> instrumentFills = accountFills.computeIfAbsent(fill.getInstrumentNode(), k -> new ArrayList<>());
            instrumentFills.add(fill);
        }

        if (gotNewAccount) {
            raiseAccountAdded(account);
        }
    }

    public void sendTransaction(Transaction transaction) {
        sendTransactionImpl(transaction);
    }

    protected abstract void sendTransactionImpl(Transaction transaction);
}
*/
