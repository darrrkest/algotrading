package com.example.quik;

import com.example.quik.messages.QLMessage;

public record QLMessageEventArgs(Object source, QLMessage message) {
}
