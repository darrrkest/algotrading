package com.example.config;

import com.example.abstractions.symbology.InstrumentDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class ConfigLoader {

    @Bean
    public List<InstrumentDefinition> instrumentDefinitions() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("instruments.json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(is);
            JsonNode instrumentsNode = rootNode.get("Instruments");
            return mapper.readerForListOf(InstrumentDefinition.class).readValue(instrumentsNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load instruments.json", e);
        }
    }
}