package org.poo.bank.commands.types.transactions.transactionHistory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to handle a group of TransactionData objects.
 */
public final class TransactionGroup {
    private final List<TransactionData> transactionGroup;

    /**
     * This constructor initializes the TransactionData List.
     */
    public TransactionGroup() {
        transactionGroup = new ArrayList<>();
    }

    /**
     * This method adds a TransactionData object to the List.
     * @param transaction The TransactionData to add.
     */
    public void addTransaction(final TransactionData transaction) {
        transactionGroup.add(transaction);
    }

    /**
     * This method takes the data of all transactions and stores it in an ArrayNode.
     * @return An ArrayNode containing all the transactions.
     */
    public ArrayNode toJson() {
        ArrayNode transactions = Bank.getInstance().createArrayNode();
        for (TransactionData data : transactionGroup) {
            transactions.add(data.toJson());
        }
        return transactions;
    }
}
