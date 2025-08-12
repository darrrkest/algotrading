package com.example.service;

import com.example.abstractions.connector.ConnectorType;
import com.example.abstractions.symbology.*;
import com.example.config.InstrumentDefinition;
import com.example.config.SymbolTemplate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public final class InstrumentServiceImpl implements InstrumentService {

    private static final Logger log = LoggerFactory.getLogger(InstrumentServiceImpl.class);
    private final Object lock = new Object();
    private final Map<Instrument, InstrumentType> types = new HashMap<>();
    private final Map<Instrument, String> exchanges = new HashMap<>();
    private List<Instrument> instruments;

    public InstrumentServiceImpl(List<InstrumentDefinition> instrumentDefinitions) {
        loadInstrumentsFromConfig(instrumentDefinitions);
    }

    public void loadInstrumentsFromConfig(List<InstrumentDefinition> instrumentDefinitions) {
        List<Instrument> instruments = new ArrayList<>();

        for (InstrumentDefinition def : instrumentDefinitions) {
            SymbolTemplate template = new SymbolTemplate(def.getCodeTemplate());

            for (String expirationStr : def.getExpirations()) {
                LocalDate expiration = LocalDate.parse(expirationStr);
                String code = template.render(def.getRoot(), def.getExchange(), expiration);

                var type = Enum.valueOf(InstrumentType.class, def.getType());
                var exchange = def.getExchange();

                Instrument instrument = new Instrument(
                        type,
                        def.getDescription(),
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

    @Override
    @NotNull
    public List<Instrument> getKnownInstruments() {
        synchronized (lock) {
            return new ArrayList<>(instruments);
        }
    }

    @Override
    public boolean isInstrumentKnown(@NotNull Instrument instrument) {
        synchronized (lock) {
            return instruments.contains(instrument);
        }
    }

    @Override
    public InstrumentType getInstrumentType(@NotNull Instrument instrument) {
        synchronized (lock) {
            return types.get(instrument);
        }
    }

    @Override
    public String getInstrumentExchange(@NotNull Instrument instrument) {
        synchronized (lock) {
            return exchanges.getOrDefault(instrument, "");
        }
    }

    @Override
    public Instrument resolveInstrument(@NotNull String code, @NotNull String exchange) {
        var symbol = String.format("%s:%s", exchange, code);
        synchronized (lock) {
            return instruments.stream().filter(e -> e.getSymbol().equals(symbol)).findFirst().orElse(null);
        }
    }
}