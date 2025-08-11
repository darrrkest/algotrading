package com.example.abstractions.connector.messages.incoming;

import com.example.abstractions.execution.OrderOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Котировка из стакана
 */
@Builder
public record OrderBookItem(@NotNull OrderOperation operation, double price, long quantity) {
}
