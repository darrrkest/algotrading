package com.example.connector.messages.outgoing;

import com.example.connector.TransactionVisitor;
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
