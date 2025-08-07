package com.example.quik.adapter;

import com.example.quik.adapter.messages.QLMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class QLAdapterMessageEventArgs extends ApplicationEvent {
    private final QLMessage message;
    public QLAdapterMessageEventArgs(Object source, QLMessage message) {
        super(source);
        this.message = message;
    }
}
