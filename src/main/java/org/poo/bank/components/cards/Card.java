package org.poo.bank.components.cards;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

/**
 * This class stores and handles a Card's information.
 */
@Data
public class Card implements Visitable {
    protected String iban;
    protected String cardNumber;
    protected CardStatus status;

    /**
     * This enum is used to determine the card status.
     * It also stores the string equivalent for ease of use.
     */
    public enum CardStatus {
        ACTIVE("active"),
        FROZEN("frozen");

        private final String status;

        CardStatus(final String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param iban The IBAN of the Account the Card is attached to.
     */
    public Card(final String iban) {
        this.iban = iban;
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param commandInput The commandInput containing Card information.
     */
    public Card(final CommandInput commandInput) {
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * The pay method handles all behaviors for frozen cards, insufficient funds,
     * and replacing oneTimeCards. It also adds the corresponding transactions
     * to the databaseEntry for every scenario.
     * @param entry The databaseEntry containing the Card.
     * @param amount The amount to be paid.
     * @param requestedCurrency The currency of the requestedAmount.
     * @param timestamp The timestamp of the payment.
     * @param commerciant The commerciant of the payment.
     */
    public void pay(final DatabaseEntry entry, final double amount,
                            final String requestedCurrency,
                            final int timestamp, final String commerciant) {
        if (status == CardStatus.FROZEN) {
            entry.addTransaction(frozenCardTransaction(timestamp));
            return;
        }

        Account account = entry.getAccount(iban);
        double sumPaid = entry.getAccount(iban).pay(amount, requestedCurrency);

        if (Double.compare(sumPaid, 0.0) == 0) {
            entry.addTransaction(insufficientFundsTransaction(timestamp));
        } else if (account.getBalance() <= account.getMinBalance()) {
            entry.addTransaction(insufficientFundsTransaction(timestamp));
        } else {
            entry.addTransaction(paymentTransaction(timestamp, sumPaid, commerciant));
        }
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertCard(this);
    }

    /**
     * This method provides the output for when a card is destroyed.
     * @param timestamp The timestamp of the destruction.
     * @return The TransactionData object containing the information.
     */
    public TransactionData destructionTransaction(final DatabaseEntry entry, final int timestamp) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "The card has been destroyed");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return new TransactionData(output, iban);
    }

    /**
     * This method provides the output for when a card is created.
     * @param timestamp The timestamp of the creation.
     * @return A TransactionData containing the information.
     */
    public TransactionData creationTransaction(final DatabaseEntry entry, final int timestamp) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return new TransactionData(output, iban);
    }

    /**
     * This method provides a TransactionData corresponding to a successful payment.
     * @param timestamp The timestamp of the payment.
     * @param amount The amount that was paid.
     * @param commerciant The commerciant of the payment.
     * @return A TransactionData containing the information.
     */
    public final TransactionData paymentTransaction(final int timestamp,
                                         final double amount,
                                         final String commerciant) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("amount", amount);
        transaction.put("commerciant", commerciant);
        transaction.put("description", "Card payment");

        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a TransactionData corresponding to a card getting frozen.
     * @param timestamp The timestamp of the payment.
     * @return A TransactionData containing the information.
     */
    public final TransactionData freezeCardTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("description",
                "You have reached the minimum amount of funds, the card will be frozen");

        status = CardStatus.FROZEN;
        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a TransactionData corresponding to an insufficientFunds error.
     * @param timestamp The timestamp of the payment.
     * @return A TransactionData containing the information.
     */
    public final TransactionData insufficientFundsTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("description", "Insufficient funds");

        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a TransactionData corresponding to a card being frozen.
     * @param timestamp The timestamp of the payment.
     * @return A TransactionData containing the information.
     */
    public final TransactionData frozenCardTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("description", "The card is frozen");

        return new TransactionData(transaction, iban);
    }
}
