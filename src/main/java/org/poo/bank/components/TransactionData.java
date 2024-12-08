package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class TransactionData {
    private final ObjectNode data;
    private final String account;

    public TransactionData(ObjectNode data, String account) {
        this.data = data;
        this.account = account;
    }
}
