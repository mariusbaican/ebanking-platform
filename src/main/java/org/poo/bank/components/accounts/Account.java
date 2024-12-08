package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.Card;
import org.poo.bank.currency.Currencies;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Account {
    protected String currency;
    protected String iban;
    protected double balance;
    protected double minBalance;
    protected String accountType;
    protected String owner;
    protected List<String> aliases;

    public Account() { }

    public Account(final CommandInput commandInput, final String iban) {
        currency = commandInput.getCurrency();
        this.iban = iban;
        balance = 0;
        minBalance = 0;
        accountType = commandInput.getAccountType();
        owner = commandInput.getEmail();
        aliases = new ArrayList<>();
    }

    public abstract boolean addInterest();

    public abstract boolean setInterest(double interestRate);

    public final double pay(final double amount, final String currency) {
        double adjustedAmount =
                amount * CurrencyExchanger.getRate(new Currencies<>(currency, this.currency));
        if (adjustedAmount > balance) {
            return 0.0;
        }
        balance -= adjustedAmount;
        return adjustedAmount;
    }

    public final double receive(final double amount, final String currency) {
        double adjustedAmount =
                amount * CurrencyExchanger.getRate(new Currencies<>(currency, this.currency));
        balance += adjustedAmount;
        return adjustedAmount;
    }

    public abstract boolean isSavingsAccount();

    public final ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("IBAN", iban);
        output.put("balance", balance);
        output.put("currency", currency);
        output.put("type", accountType);
        ArrayNode cardArray = Bank.getInstance().createArrayNode();
        for (String cardNumber : Database.getInstance().getEntryByAccount(iban).getCardNumbers()) {
            DatabaseEntry entry = Database.getInstance().getEntryByCard(cardNumber);
            Card card = entry.getCard(cardNumber);
            if (card.getIban().equals(iban)) {
                cardArray.add(card.toJson());
            }
        }
        output.put("cards", cardArray);
        return output;
    }
}
