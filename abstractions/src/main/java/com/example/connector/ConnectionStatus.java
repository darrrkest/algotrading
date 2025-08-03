package com.example.connector;

public enum ConnectionStatus {
    CONNECTED,

    CONNECTING,

    DISCONNECTED,

    DISCONNECTING,

    UNDEFINED;

    public static boolean isActive(ConnectionStatus status) {
        return status == CONNECTED || status == CONNECTING;
    }
}
