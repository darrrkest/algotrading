package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.ConnectorMessage;

public record ConnectorMessageEventArgs(ConnectorMessage message) {
}
