package org.poo.bank.database;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.commands.output.visitor.OutputVisitor;
import org.poo.bank.commands.output.visitor.Visitable;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionGroup;
import org.poo.bank.components.cards.Card;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.commerciants.Commerciant;
import org.poo.bank.components.commerciants.CommerciantGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * This class stores the information of an entry within the Database.
 */
@Data
public final class DatabaseEntry implements Visitable {
    private final User user;
    private final Map<String, Account> accounts;
    private final Map<String, Card> cards;
    private final List<TransactionData> transactionHistory;

    /**
     * This constructor creates a new entry based on a User.
     * @param user The User for this specific entry.
     */
    DatabaseEntry(final User user) {
        this.user = user;
        accounts = new LinkedHashMap<>();
        cards = new LinkedHashMap<>();
        transactionHistory = new ArrayList<>();
    }

    /**
     * This method adds a TransactionData object to the transactionHistory.
     * @param transactionData The TransactionData to be added.
     */
    public void addTransaction(final TransactionData transactionData) {
        transactionHistory.add(transactionData);
    }

    /**
     * This method adds an Account to this entry.
     * @param account The Account to be added.
     */
    void addAccount(final Account account) {
        accounts.putIfAbsent(account.getIban(), account);
    }

    /**
     * This method removes an Account from this entry.
     * @param iban The IBAN of the Account to be removed.
     */
    void removeAccount(final String iban) {
        accounts.remove(iban);
        removeAccountCards(iban);
    }

    /**
     * This method provides an Account from this entry.
     * @param iban The IBAN of the requested Account.
     * @return The requested account.
     */
    public Account getAccount(final String iban) {
        return accounts.getOrDefault(iban, null);
    }

    /**
     * This method adds a Card to this entry.
     * @param card The Card to be added.
     */
    void addCard(final Card card) {
        cards.putIfAbsent(card.getCardNumber(), card);
    }

    /**
     * This method removes a Card from this entry.
     * @param cardNumber The cardNumber of the Card to be removed.
     */
    void removeCard(final String cardNumber) {
        cards.remove(cardNumber);
    }

    /**
     * This method provides a Card from this entry.
     * @param cardNumber The cardNumber of the requested Card.
     * @return The requested Card.
     */
    public Card getCard(final String cardNumber) {
        return cards.getOrDefault(cardNumber, null);
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertEntry(this);
    }

    /**
     * This method removes all the Cards associated to an Account.
     * @param iban The IBAN of the Account to have its Cards removed.
     */
    private void removeAccountCards(final String iban) {
        for (Card card : cards.values()) {
            if (card.getIban().equals(iban)) {
                cards.remove(card.getIban());
            }
        }
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
     * @param iban The IBAN of the account for the report.
     * @return An ArrayNode containing all the transaction information.
     */
    public ArrayNode transactionsToJson(final int startTimestamp, final int endTimestamp,
                                        final String iban) {
        TransactionGroup transactions = new TransactionGroup();
        for (TransactionData transactionData : transactionHistory) {
            int timestamp = transactionData.data().get("timestamp").asInt();
            if (timestamp >= startTimestamp && timestamp <= endTimestamp) {
                if (transactionData.account().equals(iban)) {
                    transactions.addTransaction(transactionData);
                }
            }
        }
        return transactions.toJson();
    }

    /**
     * This method creates a JSON format account report containing the account information
     * and the transaction history between two provided timestamps.
     * @param startTimestamp The startingTimestamp of the report.
     * @param endTimestamp The endTimestamp of the report.
     * @param iban The IBAN of the target account.
     * @return
     */
    public ObjectNode accountReportJson(final int startTimestamp, final int endTimestamp,
                                        final String iban) {
        Account account = getAccount(iban);
        if (account == null) {
            return null;
        }
        ObjectNode accountInfo = Bank.getInstance().createObjectNode();
        accountInfo.put("IBAN", account.getIban());
        accountInfo.put("balance", account.getBalance());
        accountInfo.put("currency", account.getCurrency());
        accountInfo.put("transactions", transactionsToJson(startTimestamp, endTimestamp, iban));

        return accountInfo;
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
