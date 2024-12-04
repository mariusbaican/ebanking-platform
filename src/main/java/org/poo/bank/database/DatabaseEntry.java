package org.poo.bank.database;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.Card;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class DatabaseEntry {
    private User user;
    private HashMap<String, Account> accounts;
    private ArrayList<String> accountIbans; // This is garbage
    private HashMap<String, Card> cards;
    private ArrayList<String> cardNumbers;// This is garbage
    // Why are they garbage you may ask? Because if the order is wrong the tests fail
    // and maintaining HashMap.values() sorted properly is impossible :D

    public DatabaseEntry(User user) {
        this.user = user;
        accounts = new HashMap<>();
        accountIbans = new ArrayList<>();
        cards = new HashMap<>();
        cardNumbers = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.putIfAbsent(account.getIban(), account);
        accountIbans.add(account.getIban());
    }

    public void removeAccount(String iban) {
        accounts.remove(iban);
        accountIbans.remove(iban);
    }

    public void addCard(Card card) {
        cards.putIfAbsent(card.getCardNumber(), card);
        cardNumbers.add(card.getCardNumber());
    }

    public void removeCard(String cardNumber) {
        cards.remove(cardNumber);
        cardNumbers.remove(cardNumber);
    }

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().getObjectMapper().createObjectNode();
        output.put("firstName", user.getFirstName());
        output.put("lastName", user.getLastName());
        output.put("email", user.getEmail());
        ArrayNode accountsArray = Bank.getInstance().getObjectMapper().createArrayNode();
        for (String iban : accountIbans) {
            accountsArray.add(accounts.get(iban).toJson());
        }
        output.put("accounts", accountsArray);
        return output;
    }

    public void removeAccountCards(String iban) {
        for (Card card : cards.values()) {
            if (card.getIban().equals(iban)) {
                cards.remove(card.getIban());
                cardNumbers.remove(card.getIban());
            }
        }
    }
}
