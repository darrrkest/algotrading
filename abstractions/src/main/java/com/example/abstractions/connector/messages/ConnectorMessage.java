package com.example.abstractions.connector.messages;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Сообщения, которые выставляют наружу коннекторы
 */
@Getter
@SuperBuilder
public abstract class ConnectorMessage implements Serializable {

    public void accept(ConnectorMessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
