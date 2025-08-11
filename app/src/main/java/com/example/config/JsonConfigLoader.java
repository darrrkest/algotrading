package com.example.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class JsonConfigLoader {

    @Bean
    public List<InstrumentDefinition> instrumentDefinitions() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("instruments.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            JsonNode rootNode = mapper.readTree(is);
            JsonNode instrumentsNode = rootNode.get("Instruments");
            return mapper.readerForListOf(InstrumentDefinition.class).readValue(instrumentsNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load instruments.json", e);
        }
    }
}