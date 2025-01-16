package org.poo.bank.output.logs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;

public class Response {
    private final ObjectNode response;

    public Response() {
        response = Bank.getInstance().createObjectNode();
    }

    public Response addField(String fieldName, Object value) {
        response.putPOJO(fieldName, value);
        return this;
    }

    public ObjectNode asObjectNode() {
        return response;
    }

    public TransactionData asTransactionData(String iban) {
        return new TransactionData(response, iban);
    }
}
