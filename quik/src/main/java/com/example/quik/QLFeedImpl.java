package com.example.quik;

import com.example.abstractions.connector.*;
import com.example.abstractions.connector.messages.ConnectorMessage;
import com.example.abstractions.connector.messages.incoming.InstrumentParams;
import com.example.abstractions.connector.messages.incoming.OrderBook;
import com.example.abstractions.connector.messages.incoming.OrderBookItem;
import com.example.abstractions.connector.messages.incoming.Quote;
import com.example.abstractions.execution.OrderOperation;
import com.example.abstractions.symbology.ConnectorSymbolInfo;
import com.example.abstractions.symbology.Instrument;
import com.example.abstractions.symbology.InstrumentService;
import com.example.abstractions.symbology.Venue;
import com.example.quik.adapter.QLAdapter;
import com.example.quik.adapter.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public final class QLFeedImpl extends ConnectorService implements QLFeed {
    private static final Logger log = LoggerFactory.getLogger(QLFeedImpl.class);

    private final InstrumentService instrumentService;
    private final QLAdapter adapter;

    public QLFeedImpl(InstrumentService instrumentService, QLAdapter adapter, ApplicationEventPublisher eventPublisher) {
        super(eventPublisher);
        this.instrumentService = instrumentService;
        this.adapter = adapter;

        adapter.addMessageListener(this);
    }

    @Override
    public SubscriptionResult subscribeParams(Instrument instrument) {
        if (!instrumentService.isInstrumentKnown(instrument)) {
            log.debug("Instrument: {} is not known. Won't subscribe.", instrument);
        }

        adapter.sendMessage(new QLInstrumentParamsSubscriptionRequest(instrument.getCode()));

        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeParams(Instrument instrument) {
        if (instrumentService.isInstrumentKnown(instrument)) {
            adapter.sendMessage(new QLInstrumentParamsUnsubscriptionRequest(instrument.getCode()));
        }
    }

    @Override
    public SubscriptionResult subscribeOrderBook(Instrument instrument) {
        if (!instrumentService.isInstrumentKnown(instrument)) {
            log.debug("Instrument: {} is not known. Won't subscribe.", instrument);
        }

        adapter.sendMessage(new QLOrderBookSubscriptionRequest(instrument.getCode()));

        return SubscriptionResult.OK(instrument);
    }

    @Override
    public void unsubscribeOrderBook(Instrument instrument) {
        if (instrumentService.isInstrumentKnown(instrument)) {
            adapter.sendMessage(new QLOrderBookUnsubscriptionRequest(instrument.getCode()));
        }
    }

    @Override
    public void onMessageReceived(QLMessage message) {
        switch (message.getMessageType()) {
            case INSTRUMENT_PARAMS -> Handle((QLInstrumentParams) message);
            case ORDER_BOOK -> Handle((QLOrderBook) message);
        }
    }

    private void Handle(QLInstrumentParams message) {
        var symbol = new ConnectorSymbolInfo(message.getCode(), Venue.MOEX.getName());
        var instrument = instrumentService.resolveInstrument(symbol, ConnectorType.QUIK);

        if (instrument == null) {
            return;
        }

        BigDecimal priceMin = safeParseBigDecimal(message.getPriceMin());
        BigDecimal priceMax = safeParseBigDecimal(message.getPriceMax());

        var stepPrices = Arrays.asList(
                message.getStepPrice(),
                message.getStepPriceT(),
                message.getStepPriceCl(),
                message.getStepPricePrCl());

        var priceStepValue = stepPrices.stream().filter(n -> !n.equals(BigDecimal.ZERO)).findFirst().orElseThrow();

        var ip = InstrumentParams.builder()
                .instrument(instrument)
                .ask(new Quote(message.getAskPrice(), message.getAskSize()))
                .bid(new Quote(message.getBidPrice(), message.getBidSize()))
                .last(new Quote(message.getLastPrice(), 0))
                .lotSize(message.getLotSize())
                .IV(message.getVolatility())
                .priceStep(message.getPriceStep())
                .priceStepValue(priceStepValue)
                .settlement(message.getSettlement())
                .previousSettlement(message.getPreviousSettlement())
                .bottomPriceLimit(priceMin)
                .topPriceLimit(priceMax)
                .sessionEndTime(message.getEndTime())
                .openInterest(message.getOpenInterest())
                .lastUpdateTime(LocalDateTime.now())
                .build();

        raiseMessageReceived(this, ip);
    }

    private void Handle(QLOrderBook message) {
        var symbol = new ConnectorSymbolInfo(message.getInstrument(), Venue.MOEX.getName());
        var instrument = instrumentService.resolveInstrument(symbol, ConnectorType.QUIK);

        if (instrument == null) {
            return;
        }

        if (!message.hasBids() && !message.hasOffers()) {
            log.debug("Empty order book (instrument={}) received, skip.", instrument);
            return;
        }

        var obi = new ArrayList<OrderBookItem>();
        if (message.hasBids()) {
            for (QLQuote quote : message.getBids()) {
                obi.add(new OrderBookItem(OrderOperation.BUY, quote.getPrice(), quote.getSize()));
            }
        }

        if (message.hasOffers()) {
            for (QLQuote quote : message.getOffers()) {
                obi.add(new OrderBookItem(OrderOperation.SELL, quote.getPrice(), quote.getSize()));
            }
        }

        var ob = OrderBook.builder()
                .instrument(instrument)
                .items(obi)
                .build();

        raiseMessageReceived(this, ob);
    }

    public static BigDecimal safeParseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void close() {
        adapter.removeMessageListener(this);
    }
}
