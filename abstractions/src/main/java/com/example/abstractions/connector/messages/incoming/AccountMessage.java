package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.connector.messages.ConnectorMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Сообщения по конкретному счету
 */
@Getter
@SuperBuilder
public abstract class AccountMessage extends ConnectorMessage {

    @NotNull private String account;
}
