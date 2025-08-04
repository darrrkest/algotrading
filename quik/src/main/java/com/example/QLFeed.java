package com.example;

import com.example.connector.Feed;
import com.example.connector.SubscriptionResult;
import com.example.messages.QLInstrumentParamsSubscriptionRequest;
import com.example.messages.QLInstrumentParamsUnsubscriptionRequest;
import com.example.symbology.Instrument;
import com.example.symbology.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface QLFeed extends Feed, AdapterMessageConsumer {

}
