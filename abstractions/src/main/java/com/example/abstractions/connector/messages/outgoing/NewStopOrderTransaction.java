package com.example.abstractions.connector.messages.outgoing;

import com.example.abstractions.connector.TransactionVisitor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
// TODO
public class NewStopOrderTransaction extends Transaction {
    @Override
    void Visit(TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
