package com.example.quik;

import com.example.quik.messages.QLEnvelope;
import com.example.quik.messages.QLHeartbeat;
import com.example.quik.messages.QLMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class QLMapper {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    static String serialize(QLMessage message) {
        try {
            return mapper.writeValueAsString(message);
        }
        catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static QLEnvelope deserializeEnvelope(String envelopeJson) {
        try {
            return mapper.readValue(envelopeJson, QLEnvelope.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String heartbeatJson() {
        try {
            return mapper.writeValueAsString(new QLHeartbeat());
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    //public static QLStopOrderStateChange deserializeStopOrderStateChange(String json) {
    //    try {
    //        return mapper.readValue(json, QLStopOrderStateChange.class);
    //    } catch (JsonProcessingException e) {
    //        throw new RuntimeException(e);
    //    }
    //}
}
