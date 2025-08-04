package com.example;

import com.example.connector.ConnectorMessageConsumer;
import com.example.connector.ConnectorType;
import com.example.connector.SubscriptionResult;
import com.example.connector.messages.ConnectorMessage;
import com.example.connector.messages.incoming.InstrumentParams;
import com.example.connector.messages.incoming.OrderBook;
import com.example.connector.messages.incoming.OrderBookItem;
import com.example.connector.messages.incoming.Quote;
import com.example.execution.OrderOperation;
import com.example.messages.*;
import com.example.symbology.ConnectorSymbolInfo;
import com.example.symbology.Instrument;
import com.example.symbology.InstrumentService;
import com.example.symbology.Venue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class QLFeedImpl implements QLFeed {
    private final Logger log = LoggerFactory.getLogger(QLFeedImpl.class);

    private final InstrumentService instrumentService;
    private final QLAdapter adapter;

    public QLFeedImpl(InstrumentService instrumentService, QLAdapter adapter) {
        this.instrumentService = instrumentService;
        this.adapter = adapter;

        adapter.subscribe(this);
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
    public void HandleAdapterMessage(QLMessage message) {
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

        raiseMessageReceived(ip);
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

        raiseMessageReceived(ob);
    }

    public static BigDecimal safeParseBigDecimal(String s) {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private final List<ConnectorMessageConsumer> subs = new CopyOnWriteArrayList<>();

    @Override
    public void addMessageListener(ConnectorMessageConsumer listener) {
        subs.add(listener);
    }

    @Override
    public void removeMessageListener(ConnectorMessageConsumer listener) {
        subs.remove(listener);
    }

    private void raiseMessageReceived(ConnectorMessage message) {
        ConnectorMessageEventArgs event = new ConnectorMessageEventArgs(message);
        for (ConnectorMessageConsumer listener : subs) {
            listener.handle(event);
        }
    }
}
