package com.example.service;

import com.example.symbology.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public final class InstrumentServiceImpl implements InstrumentService {

    private final Logger log = LoggerFactory.getLogger(InstrumentServiceImpl.class);
    private final Object lock = new Object();
    private Map<Instrument, InstrumentType> types = new HashMap<>();
    private Map<Instrument, String> exchanges = new HashMap<>();
    private List<Instrument> instruments;

    public InstrumentServiceImpl(List<InstrumentDefinition> instrumentDefinitions) {
        loadInstrumentsFromConfig(instrumentDefinitions);
    }

    public void loadInstrumentsFromConfig(List<InstrumentDefinition> instrumentDefinitions) {
        List<Instrument> instruments = new ArrayList<>();

        for (InstrumentDefinition raw : instrumentDefinitions) {
            SymbolTemplate template = new SymbolTemplate(raw.getCodeTemplate());

            for (String expirationStr : raw.getExpirations()) {
                LocalDate expiration = LocalDate.parse(expirationStr);
                String code = template.render(raw.getRoot(), raw.getExchange(), expiration);

                var type = Enum.valueOf(InstrumentType.class, raw.getType());
                var exchange = raw.getExchange();

                Instrument instrument = new Instrument(
                        raw.getRoot(),
                        type,
                        raw.getDescription(),
                        exchange,
                        expiration,
                        code
                );

                types.put(instrument, type);
                exchanges.put(instrument, exchange);

                instruments.add(instrument);
            }
        }

        this.instruments = instruments;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    @Override
    public List<Instrument> getKnownInstruments() {
        synchronized (lock) {
            return new ArrayList<>(instruments);
        }
    }

    @Override
    public boolean isInstrumentKnown(Instrument instrument) {
        synchronized (lock) {
            return instruments.contains(instrument);
        }
    }

    @Override
    public InstrumentType getInstrumentType(Instrument instrument) {
        synchronized (lock) {
            return types.get(instrument);
        }
    }

    @Override
    public String getInstrumentExchange(Instrument instrument) {
        synchronized (lock) {
            return exchanges.getOrDefault(instrument, "");
        }
    }
}