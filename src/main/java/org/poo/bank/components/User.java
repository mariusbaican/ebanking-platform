package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionGroup;
import org.poo.bank.components.commerciants.Commerciant;
import org.poo.bank.components.commerciants.CommerciantGroup;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store and handle a User's information.
 */
@Data
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<TransactionData> transactionHistory;

    /**
     * This constructor creates a User object based on a provided input.
     * @param userInput The desired User information.
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * This method adds a TransactionData object to the transactionHistory.
     * @param transactionData The TransactionData to be added.
     */
    public void addTransaction(final TransactionData transactionData) {
        transactionHistory.add(transactionData);
    }

    /**
     * This method creates a spending report for this User instance.
     * @param startTimestamp The startTimestamp for the report.
     * @param endTimestamp The endTimestamp for the report.
     * @param account The IBAN of the account for the report.
     * @param output The ObjectNode for the report to be added into.
     */
    public void spendingsReportJson(final int startTimestamp, final int endTimestamp,
                                    final String account, final ObjectNode output) {
        TransactionGroup transactions = new TransactionGroup();
        CommerciantGroup commerciants = new CommerciantGroup();
        for (TransactionData transactionData : transactionHistory) {
            int timestamp = transactionData.data().get("timestamp").asInt();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                if (transactionData.account().equals(account)) {
                    if (transactionData.data().has("commerciant")) {
                        transactions.addTransaction(transactionData);
                        commerciants.addCommerciant(
                                new Commerciant(transactionData.data().get("commerciant").asText(),
                                        transactionData.data().get("amount").asDouble()));
                    }
                }
            }
        }
        output.put("transactions", transactions.toJson());
        output.put("commerciants", commerciants.toJson());
    }

    /**
     * This method creates a transactionReport for this User instance.
     * @param startTimestamp The startTimestamp for the report.
     * @param endTimestamp The endTimestamp for the report.
     * @param account The IBAN of the account for the report.
     * @return An ArrayNode containing all the transaction information.
     */
    public ArrayNode transactionsToJson(final int startTimestamp, final int endTimestamp,
                                        final String account) {
        TransactionGroup transactions = new TransactionGroup();
        for (TransactionData transactionData : transactionHistory) {
            int timestamp = transactionData.data().get("timestamp").asInt();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                if (transactionData.account().equals(account)) {
                    transactions.addTransaction(transactionData);
                }
            }
        }
        return transactions.toJson();
    }

    /**
     * This method creates a transactionReport for this User instance.
     * @return An ArrayNode containing all the transaction information.
     */
    public ArrayNode transactionsToJson() {
        TransactionGroup transactions = new TransactionGroup();
        for (TransactionData transactionData : transactionHistory) {
                transactions.addTransaction(transactionData);
        }
        return transactions.toJson();
    }
}
