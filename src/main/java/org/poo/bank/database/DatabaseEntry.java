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

/**
 * This class stores the information of an entry within the Database.
 */
@Data
public final class DatabaseEntry {
    private User user;
    private Map<String, Account> accounts;
    private List<String> accountIbans; // This is garbage
    private Map<String, Card> cards;
    private List<String> cardNumbers; // This is garbage
    // The garbage is here to keep the accounts and cards sorted in chronological
    // order to pass the tests. You might not like it, I don't either :/

    /**
     * This constructor creates a new entry based on a User.
     * @param user The User for this specific entry.
     */
    public DatabaseEntry(final User user) {
        this.user = user;
        accounts = new HashMap<>();
        accountIbans = new ArrayList<>();
        cards = new HashMap<>();
        cardNumbers = new ArrayList<>();
    }

    /**
     * This method adds an Account to this entry.
     * @param account The Account to be added.
     */
    public void addAccount(final Account account) {
        accounts.putIfAbsent(account.getIban(), account);
        accountIbans.add(account.getIban());
    }

    /**
     * This method removes an Account from this entry.
     * @param iban The IBAN of the Account to be removed.
     */
    public void removeAccount(final String iban) {
        accounts.remove(iban);
        accountIbans.remove(iban);
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
    public void addCard(final Card card) {
        cards.putIfAbsent(card.getCardNumber(), card);
        cardNumbers.add(card.getCardNumber());
    }

    /**
     * This method removes a Card from this entry.
     * @param cardNumber The cardNumber of the Card to be removed.
     */
    public void removeCard(final String cardNumber) {
        cards.remove(cardNumber);
        cardNumbers.remove(cardNumber);
    }

    /**
     * This method provides a Card from this entry.
     * @param cardNumber The cardNumber of the requested Card.
     * @return The requested Card.
     */
    public Card getCard(final String cardNumber) {
        return cards.getOrDefault(cardNumber, null);
    }

    /**
     * This method converts an Entry's information to JSON format.
     * @return An ObjectNode containing the Entry's information.
     */
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

    /**
     * This method removes all the Cards associated to an Account.
     * @param iban The IBAN of the Account to have its Cards removed.
     */
    public void removeAccountCards(final String iban) {
        for (Card card : cards.values()) {
            if (card.getIban().equals(iban)) {
                cards.remove(card.getIban());
                cardNumbers.remove(card.getIban());
            }
        }
    }
}
