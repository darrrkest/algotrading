package com.example;

import com.example.symbology.Instrument;
import com.example.symbology.InstrumentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AlgotradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlgotradingApplication.class, args);
    }

    @Bean
    public QLAdapter qlAdapter() {
        return new QLAdapterImpl("localhost", 1250);
    }

    @Bean
    public QLFeedImpl qlFeed(InstrumentService instrumentService, QLAdapter adapter) {
        adapter.start();
        var feed = new QLFeedImpl(instrumentService, adapter);
        feed.subscribeParams(new Instrument("SiU5"));
        feed.subscribeParams(new Instrument("GDU5"));
        return feed;
    }

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
}