package com.example.connector;

import com.example.connector.messages.ConnectorMessage;

public interface ConnectorMessageVisitor {
    void visit(ConnectorMessage message);
}