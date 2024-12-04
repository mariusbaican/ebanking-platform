package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.Card;
import org.poo.bank.components.User;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Data
public abstract class Account {
    protected String currency;
    protected String iban;
    protected double balance;
    protected double minBalance;
    protected String accountType;
    protected User owner;
    protected ArrayList<String> aliases;

    public Account() { }

    public Account(CommandInput commandInput, String iban) {
        currency = commandInput.getCurrency();
        this.iban = iban;
        balance = 0;
        minBalance = -1;
        accountType = commandInput.getAccountType();
        owner = null;
        aliases = new ArrayList<>();
    }

    public abstract boolean addInterest();

    public abstract boolean setInterest(double interestRate);

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().getObjectMapper().createObjectNode();
        output.put("IBAN", iban);
        output.put("balance", balance);
        output.put("currency", currency);
        output.put("type", accountType);
        ArrayNode cardArray = Bank.getInstance().getObjectMapper().createArrayNode();
        for (String cardNumber : Database.getInstance().getEntryByAccount(iban).getCardNumbers()) {
            cardArray.add(Database.getInstance().getCard(cardNumber).toJson());
        }
        output.put("cards", cardArray);
        return output;
    }
}
