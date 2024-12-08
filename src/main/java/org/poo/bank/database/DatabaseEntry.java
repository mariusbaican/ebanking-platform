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
import java.util.List;
import java.util.Map;

@Data
public final class DatabaseEntry {
    private User user;
    private Map<String, Account> accounts;
    private List<String> accountIbans; // This is garbage
    private Map<String, Card> cards;
    private List<String> cardNumbers; // This is garbage
    // The garbage is here to keep the accounts and cards sorted in chronological
    // order to pass the tests. You might not like it, I don't either :/

    public DatabaseEntry(final User user) {
        this.user = user;
        accounts = new HashMap<>();
        accountIbans = new ArrayList<>();
        cards = new HashMap<>();
        cardNumbers = new ArrayList<>();
    }

    public void addAccount(final Account account) {
        accounts.putIfAbsent(account.getIban(), account);
        accountIbans.add(account.getIban());
    }

    public void removeAccount(final String iban) {
        accounts.remove(iban);
        accountIbans.remove(iban);
    }

    public Account getAccount(final String iban) {
        return accounts.get(iban);
    }

    public void addCard(final Card card) {
        cards.putIfAbsent(card.getCardNumber(), card);
        cardNumbers.add(card.getCardNumber());
    }

    public void removeCard(final String cardNumber) {
        cards.remove(cardNumber);
        cardNumbers.remove(cardNumber);
    }

    public Card getCard(final String cardNumber) {
        return cards.get(cardNumber);
    }

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("firstName", user.getFirstName());
        output.put("lastName", user.getLastName());
        output.put("email", user.getEmail());
        ArrayNode accountsArray = Bank.getInstance().createArrayNode();
        for (String iban : accountIbans) {
            accountsArray.add(accounts.get(iban).toJson());
        }
        output.put("accounts", accountsArray);
        return output;
    }

    public void removeAccountCards(final String iban) {
        for (Card card : cards.values()) {
            if (card.getIban().equals(iban)) {
                cards.remove(card.getIban());
                cardNumbers.remove(card.getIban());
            }
        }
    }
}
