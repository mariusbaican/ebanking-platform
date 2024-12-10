package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.Card;
import org.poo.bank.currency.Currencies;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

/**
 * This class stores and handles an Account's data.
 */
@Data
public abstract class Account {
    protected String currency;
    protected String iban;
    protected double balance;
    protected double minBalance;
    protected String accountType;
    protected String owner;

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
        accountType = commandInput.getAccountType();
        owner = commandInput.getEmail();
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
     * This method specifies if an account is a SavingsAccount.
     * @return True if it's a SavingsAccount. False if it's a ClassicAccount.
     */
    public abstract boolean isSavingsAccount();

    /**
     * This method handles a payment for this account instance.
     * @param amount The amount to be paid.
     * @param requestedCurrency The original currency to be paid.
     * @return The amount that was paid in the account's currency. 0.0 if insufficient funds.
     */
    public final double pay(final double amount, final String requestedCurrency) {
        double adjustedAmount = amount
                * CurrencyExchanger.getRate(new Currencies<>(requestedCurrency, this.currency));
        if (adjustedAmount > balance) {
            return 0.0;
        }
        balance -= adjustedAmount;
        return adjustedAmount;
    }

    /**
     * This method handles the receiving of funds for this account instance.
     * @param amount The amount to be received.
     * @param requestedCurrency The original currency to be received.
     * @return The amount that was received in the account's currency.
     */
    public final double receive(final double amount, final String requestedCurrency) {
        double adjustedAmount = amount
                * CurrencyExchanger.getRate(new Currencies<>(requestedCurrency, this.currency));
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
        double adjustedAmount = amount
                * CurrencyExchanger.getRate(new Currencies<>(requestedCurrency, this.currency));
        if (Double.compare(balance, adjustedAmount) >= 0) {
            return adjustedAmount;
        }
        return 0.0;
    }

    /**
     * This method converts an account's information to JSON format.
     * @return An ObjectNode containing the account information.
     */
    public final ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("IBAN", iban);
        output.put("balance", balance);
        output.put("currency", currency);
        output.put("type", accountType);
        ArrayNode cardArray = Bank.getInstance().createArrayNode();
        for (String cardNumber : Bank.getInstance().getDatabase()
                .getEntryByUser(owner).getCardNumbers()) {
            DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByCard(cardNumber);
            if (entry == null) {
                return null;
            }
            Card card = entry.getCard(cardNumber);
            if (card.getIban().equals(iban)) {
                cardArray.add(card.toJson());
            }
        }
        output.put("cards", cardArray);
        return output;
    }
}
