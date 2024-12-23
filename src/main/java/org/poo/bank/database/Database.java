package org.poo.bank.database;

import lombok.Data;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;
import org.poo.bank.components.cards.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.User;
import org.poo.fileio.UserInput;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class is used to store and access the data of the Bank.
 * It uses an access approach similar to SQL (for immersion I guess?)
 */
@Data
public final class Database implements Visitable {
    private final Map<String, DatabaseEntry> userDB;
    private final Map<String, DatabaseEntry> accountDB;
    private final Map<String, DatabaseEntry> cardDB;
    private final Map<String, String> aliases;

    /**
     * This constructor initializes the data structures.
     */
    public Database() {
        userDB = new LinkedHashMap<>();
        accountDB = new LinkedHashMap<>();
        cardDB = new LinkedHashMap<>();
        aliases = new LinkedHashMap<>();
    }

    /**
     * This method clears the Database contents.
     */
    public void reset() {
        userDB.clear();
        accountDB.clear();
        cardDB.clear();
        aliases.clear();
    }

    /**
     * This method is used to add the Users to the Database.
     * @param userInput The list of active Users.
     */
    public void addUsers(final UserInput[] userInput) {
        for (UserInput user : userInput) {
            userDB.put(user.getEmail(), new DatabaseEntry(new User(user)));
        }
    }

    /**
     * This method is used to add an Alias for an existing account.
     * @param alias The desired Alias.
     * @param iban The IBAN of the account for the Alias to represent.
     */
    public void addAlias(final String alias, final String iban) {
        if (getAccount(iban) != null) {
            aliases.putIfAbsent(alias, iban);
        }
    }

    /**
     * This method is used to add an Account to the Database.
     * @param email The email of the Account owner.
     * @param account The account to be added.
     */
    public void addAccount(final String email, final Account account) {
        DatabaseEntry entry = userDB.getOrDefault(email, null);
        if (entry == null) {
            return;
        }
        entry.addAccount(account);
        accountDB.put(account.getIban(), entry);
    }

    /**
     * This method is used to remove an Account from the Database.
     * @param iban The IBAN of the Account to be removed.
     */
    public void removeAccount(final String iban) {
        DatabaseEntry entry = getEntryByAccount(iban);
        if (entry == null) {
            return;
        }
        entry.removeAccount(iban);
        accountDB.remove(iban);
    }

    /**
     * This method is used to add a Card to the Database.
     * @param email The email of the Card owner.
     * @param card The Card to be added.
     */
    public void addCard(final String email, final Card card) {
        DatabaseEntry entry = userDB.getOrDefault(email, null);
        if (entry == null) {
            return;
        }
        entry.addCard(card);
        cardDB.put(card.getCardNumber(), entry);
    }

    /**
     * This method is used to remove a Card from the Database.
     * @param cardNumber The cardNumber of the Card to be removed.
     */
    public void removeCard(final String cardNumber) {
        DatabaseEntry entry = getEntryByCard(cardNumber);
        if (entry == null) {
            return;
        }
        entry.removeCard(cardNumber);
        cardDB.remove(cardNumber);
    }

    // The following methods showcase what I meant by SQL-type data access*
    /**
     * This method pulls an entry from the Database based on its specific User.
     * @param email The email of the desired User.
     * @return The DatabaseEntry of the provided User.
     */
    public DatabaseEntry getEntryByUser(final String email) {
        return userDB.getOrDefault(email, null);
    }

    /**
     * This method pulls an entry from the Database based on a specific Account.
     * @param input The IBAN or Alias of the desired Account.
     * @return The DatabaseEntry of the provided Account.
     */
    public DatabaseEntry getEntryByAccount(final String input) {
        //This allows for the use of aliases instead of Account IBAN
        String actualIban = aliases.getOrDefault(input, null);
        if (actualIban == null) {
            actualIban = input;
        }
        return accountDB.get(actualIban);
    }

    /**
     * This method pulls an entry from the Database based on a specific Card.
     * @param cardNumber The cardNumber of the desired Card.
     * @return The DatabaseEntry of the provided Card.
     */
    public DatabaseEntry getEntryByCard(final String cardNumber) {
        return cardDB.get(cardNumber);
    }

    // This may be unused, but it's here for the sake of having
    // add, remove and get methods for everything
    /**
     * This method pulls a User from the Database.
     * @param email The email of the requested User.
     * @return The requested User.
     */
    public User getUser(final String email) {
        if (!userDB.containsKey(email)) {
            return null;
        }
        return userDB.get(email).getUser();
    }

    /**
     * This method pulls an Account from the Database.
     * @param input The IBAN or Alias of the requested Account.
     * @return The requested Account.
     */
    public Account getAccount(final String input) {
        //This allows for the use of aliases instead of Account IBAN
        String actualIban = aliases.getOrDefault(input, null);
        if (actualIban == null) {
            actualIban = input;
        }
        if (!accountDB.containsKey(actualIban)) {
            return null;
        }
        return accountDB.get(actualIban).getAccount(actualIban);
    }

    // This may be unused, but it's here for the sake of having
    // add, remove and get methods for everything
    /**
     * This method pulls a Card from the Database.
     * @param cardNumber The cardNumber of the requested Card.
     * @return The requested Card.
     */
    public Card getCard(final String cardNumber) {
        if (!cardDB.containsKey(cardNumber)) {
            return null;
        }
        return cardDB.get(cardNumber).getCard(cardNumber);
    }

    public Collection<DatabaseEntry> getEntries() {
        return userDB.values();
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertDatabase(this);
    }
}
