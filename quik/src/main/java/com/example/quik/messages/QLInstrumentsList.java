package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeName("InstrumentsList")
public final class QLInstrumentsList extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.INSTRUMENTS_LIST;
    }

    private String futures;
    private String options;

    private List<String> futuresCodes = Collections.emptyList();
    private List<String> optionsCodes = Collections.emptyList();

    @JsonProperty("futures")
    public String getFutures() {
        return futures;
    }

    @JsonProperty("futures")
    public void setFutures(String futures) {
        this.futures = futures;
        if (futures != null && !futures.isEmpty()) {
            this.futuresCodes = Arrays.asList(futures.split(","));
        } else {
            this.futuresCodes = Collections.emptyList();
        }
    }

    @JsonIgnore
    public List<String> getFuturesCodes() {
        return futuresCodes;
    }

    @JsonProperty("options")
    public String getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(String options) {
        this.options = options;
        if (options != null && !options.isEmpty()) {
            this.optionsCodes = Arrays.asList(options.split(","));
        } else {
            this.optionsCodes = Collections.emptyList();
        }
    }

    @JsonIgnore
    public List<String> getOptionsCodes() {
        return optionsCodes;
    }
}
