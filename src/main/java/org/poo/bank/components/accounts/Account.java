package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;
import org.poo.bank.output.logs.TransactionData;
import org.poo.bank.components.ServicePlanHandler.ServicePlan;
import org.poo.bank.components.User;
import org.poo.bank.Tuple;
import org.poo.bank.components.commerciants.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class stores and handles an Account's data.
 */
@Data
public abstract class Account implements Visitable {
    protected String currency;
    protected String iban;
    protected double balance;
    protected double minBalance;
    protected AccountType accountType;
    protected String owner;
    protected ServicePlan servicePlan;
    protected Set<Commerciant> commerciantHistory;

    public enum AccountType {
        CLASSIC("classic"),
        SAVINGS("savings"),
        BUSINESS("business");

        String type;

        AccountType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public Account() { }

    /**
     * This constructor creates an account based on a command's input.
     * @param commandInput The desired account information.
     */
    public Account(final CommandInput commandInput) {
        currency = commandInput.getCurrency();
        this.iban = Utils.generateIBAN();
        balance = 0;
        minBalance = 0;
        accountType = fromString(commandInput.getAccountType());
        owner = commandInput.getEmail();
        commerciantHistory = new TreeSet<>(Comparator.comparing(Commerciant::getName));
        User user = Bank.getInstance().getDatabase().getUser(commandInput.getEmail());
        if (user == null)
            return;
        if (user.getOccupation().equals(ServicePlan.STUDENT.toString())) {
            servicePlan = ServicePlan.STUDENT;
            return;
        }
        servicePlan = ServicePlan.STANDARD;
    }

    /**
     * This method is used to add the interest to a SavingsAccount
     * @return True if it's a SavingsAccount. False if it's a ClassicAccount.
     */
    public abstract boolean addInterest();

    /**
     * This method is used to set the interest of a SavingsAccount
     * @return True if it's a SavingsAccount. False if it's a ClassicAccount.
     */
    public abstract boolean setInterest(double interestRate);

    /**
     * This method handles a payment for this account instance.
     * @param amount The amount to be paid.
     * @param requestedCurrency The original currency to be paid.
     * @return The amount that was paid in the account's currency. 0.0 if insufficient funds.
     */
    public final double pay(final double amount, final String requestedCurrency) {
        double adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(requestedCurrency, currency), amount);
        if (!canAfford(adjustedAmount)) {
            return 0.0;
        }
        balance -= adjustedAmount;
        return adjustedAmount;
    }

    public final void addFunds(final double amount) {
        balance += amount;
    }

    public final double withdrawFunds(final double amount, final String currency) {
        double adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(currency, this.currency), amount);
        if (!canAfford(adjustedAmount)) {
            return 0.0;
        }
        balance -= adjustedAmount;
        return adjustedAmount;
    }

    /**
     * This method handles the receiving of funds for this account instance.
     * @param amount The amount to be received.
     * @param receivedCurrency The original currency to be received.
     * @return The amount that was received in the account's currency.
     */
    public final double receive(final double amount, final String receivedCurrency) {
        double adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(receivedCurrency, currency), amount);
        balance += adjustedAmount;
        return adjustedAmount;
    }

    /**
     * This method specifies if a payment can be fulfilled.
     * @param amount The amount to be paid.
     * @param requestedCurrency The original currency to be paid.
     * @return The amount to be paid in the account's currency. 0.0 if insufficient funds.
     */
    public final double canAfford(final double amount, final String requestedCurrency) {
        double adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(requestedCurrency, currency), amount);
        if (Double.compare(balance, adjustedAmount) >= 0) {
            return adjustedAmount;
        }
        return 0.0;
    }

    private boolean canAfford(final double amount) {
        return Double.compare(balance, amount) >= 0;
    }

    /**
     * This method provides a TransactionData object for a newly created account.
     * @param timestamp The timestamp of the request.
     * @return A TransactionData object containing the information.
     */
    public final TransactionData newAccountTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("description", "New account created");

        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a TransactionData object for an insufficientFunds error.
     * @param timestamp The timestamp of the request.
     * @return A TransactionData object containing the information.
     */
    public final TransactionData insufficientFundsTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("timestamp", timestamp);
        transaction.put("description", "Insufficient funds");

        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a JSON format message for an addInterest request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the information.
     */
    public abstract ObjectNode addInterestJson(int timestamp);

    /**
     * This method provides a JSON format message for a changeInterest request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the information.
     */
    public abstract ObjectNode changeInterestRateJson(int timestamp, double newInterestRate);

    /**
     * This method provides a JSON format error message in case of funds remaining when
     * deletion is requested.
     * @param timestamp The timestamp of the request.
     * @return A TransactionData object containing the error message.
     */
    public final TransactionData remainingFundsTransaction(final int timestamp) {
        ObjectNode transaction = Bank.getInstance().createObjectNode();
        transaction.put("description",
                "Account couldn't be deleted - there are funds remaining");
        transaction.put("timestamp", timestamp);

        return new TransactionData(transaction, iban);
    }

    /**
     * This method provides a JSON format message for a deleteAccount request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the output message.
     */
    public final ObjectNode deletionJson(final int timestamp) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("success", "Account deleted");
        output.put("timestamp", timestamp);

        return output;
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertAccount(this);
    }

    private AccountType fromString(String type) {
        switch (type) {
            case "classic" -> {
                return AccountType.CLASSIC;
            }
            case "savings" -> {
                return AccountType.SAVINGS;
            }
            case "business" -> {
                return AccountType.BUSINESS;
            }
            default -> throw new IllegalArgumentException("Invalid accountType " + type);
        }
    }
}
