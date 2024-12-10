package org.poo.bank.commands.types.transactions.transactionHistory;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This record is used to store the information of a transaction.
 * @param data The JSON formatted transaction data.
 * @param account The IBAN of the account the transaction is associated with.
 */
public record TransactionData(ObjectNode data, String account) {

    /**
     * This method returns the JSON formatted transaction data.
     * @return An ObjectNode containing the transaction information.
     */
    public ObjectNode toJson() {
        return data;
    }
}
