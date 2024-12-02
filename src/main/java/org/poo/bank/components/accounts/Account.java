package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.bank.components.Card;
import org.poo.bank.components.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Data
public class Account {
    private String currency;
    private String iban;
    private double balance;
    private double minBalance;
    private String accountType;
    private User owner;
    private ArrayList<Card> cards;
    private ArrayList<String> aliases;

    public Account() { }

    public Account(CommandInput commandInput, String iban) {
        currency = commandInput.getCurrency();
        this.iban = iban;
        balance = 0;
        minBalance = 0;
        accountType = commandInput.getAccountType();
        owner = null;
        cards = new ArrayList<>();
        aliases = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
