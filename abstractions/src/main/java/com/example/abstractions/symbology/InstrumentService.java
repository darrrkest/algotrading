package com.example.abstractions.symbology;

import com.example.abstractions.connector.ConnectorType;

import java.util.List;

public interface InstrumentService {
    List<Instrument> getKnownInstruments();
    boolean isInstrumentKnown(Instrument instrument);
    InstrumentType getInstrumentType(Instrument instrument);
    String getInstrumentExchange(Instrument instrument);
    Instrument resolveInstrument(ConnectorSymbolInfo info, ConnectorType connectorType);
}
