package com.example.quik.messages;

import com.example.quik.messages.transaction.QLMessageType;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonTypeName("AccountsList")
public final class QLAccountsList extends QLMessage {
    @Override
    public QLMessageType getMessageType() {
        return QLMessageType.ACCOUNTS_LIST;
    }

    private List<String> accounts;
}
