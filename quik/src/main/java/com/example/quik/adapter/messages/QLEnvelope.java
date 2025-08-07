package com.example.quik.adapter.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Конверт с несколькими сообщениями
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class QLEnvelope {

    @JsonProperty("id")
    private int id;

    @JsonProperty("count")
    private int count;

    @JsonProperty("body")
    private List<QLMessage> body;

    @Override
    public String toString() {
        return "QLEnvelope [id=" + id + ", count=" + count + "]";
    }
}
