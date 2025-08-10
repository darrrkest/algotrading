package com.example.abstractions.connector.messages.incoming;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class MoneyPosition extends AccountMessage {
    // TODO Информация о лимитах по деньгам по заданному счету
}
