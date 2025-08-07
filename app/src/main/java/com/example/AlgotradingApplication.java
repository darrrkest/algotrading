package com.example;

import com.example.abstractions.connector.Connector;
import com.example.abstractions.connector.ConnectorMessageEventArgs;
import com.example.quik.QLConnectorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AlgotradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlgotradingApplication.class, args);
    }

    /**
     *         connector.start();
     *         connector.getFeed().subscribeParams(new Instrument("SiU5"));
     */

    @Bean
    public Connector createConnector(QLConnectorFactory factory) {
        var t = factory.createConnector("localhost", 1250);
        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        t.stop();
        return t;
    }

    //@Bean
    //public QLFeedImpl qlFeed(InstrumentService instrumentService, QLAdapter adapter) {
    //    adapter.start();
    //    var feed = new QLFeedImpl(instrumentService, adapter);
    //    feed.subscribeParams(new Instrument("SiU5"));
    //    feed.subscribeOrderBook(new Instrument("GDU5"));
    //    return feed;
    //}

    //@EventListener(ApplicationReadyEvent.class)
    //public void init(QLFeedImpl feed) {
    //    var order = new Order();
    //    order.setAccount("SPBFUT000h9");
    //    order.setInstrument(new Instrument("SiU5"));
    //    order.setOperation(OrderOperation.BUY);
    //    order.setPrice(BigDecimal.valueOf(66400));
    //    order.setSize(5);
    //    order.setComment("commect");
    //    order.setGoodTill(LocalDateTime.MAX);
//
    //    var newo = NewOrderTransaction.fromOrder(order);
//
    //    //t.sendMessage(QLTransaction.fromNewOrderTransaction(newo, 435435123L));
    //    feed.subscribeParams(new Instrument("SiU5"));
    //}

    @EventListener
    public void onMessage(ConnectorMessageEventArgs event) {
        System.out.println(event);
    }
}