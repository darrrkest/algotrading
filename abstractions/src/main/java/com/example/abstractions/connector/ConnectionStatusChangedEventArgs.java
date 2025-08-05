package com.example.abstractions.connector;

public record ConnectionStatusChangedEventArgs(Object source, ConnectionStatus status) {
}
