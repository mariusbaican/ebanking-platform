package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public final class TransactionData {
    private final ObjectNode data;
    private final String account;

    public TransactionData(final ObjectNode data, final String account) {
        this.data = data;
        this.account = account;
    }
}
