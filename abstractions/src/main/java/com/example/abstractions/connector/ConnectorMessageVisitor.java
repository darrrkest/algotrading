package com.example.abstractions.connector;

import com.example.abstractions.connector.messages.ConnectorMessage;

public interface ConnectorMessageVisitor {
    void visit(ConnectorMessage message);
}