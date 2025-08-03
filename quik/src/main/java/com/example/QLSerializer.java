package com.example;

import com.example.messages.QLEnvelope;
import com.example.messages.QLHeartbeat;
import com.example.messages.QLMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class QLSerializer {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
