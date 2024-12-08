package org.poo.bank.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.User;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public final class Database {
    public static final Database DATABASE = new Database();

    private final Map<String, DatabaseEntry> db;
    // This is here to keep the users sorted in chronological order
    // to pass the tests. You might not like it, I don't either :/
    private final List<String> users;
    private final Map<String, String> aliases;

    private Database() {
        db = new HashMap<>();
        aliases = new HashMap<>();
        users = new ArrayList<>();
    }

    public void reset() {
        db.clear();
        aliases.clear();
        users.clear();
    }

    public void addAll(final ObjectInput input, final ObjectMapper objectMapper) {
        for (UserInput user : input.getUsers()) {
            db.put(user.getEmail(), new DatabaseEntry(new User(user, objectMapper)));
            users.add(user.getEmail());
        }
    }

    public static Database getInstance() {
        return DATABASE;
    }

    public void addAlias(final String alias, final String iban) {
        if (getAccount(iban) != null) {
            aliases.putIfAbsent(alias, iban);
        }
    }

    public void addAccount(final String email, final Account account) {
        DatabaseEntry entry = db.getOrDefault(email, null);
        if (entry == null) {
            return;
        }
        entry.addAccount(account);
    }

    public void removeAccount(final String iban) {
        for (DatabaseEntry entry : db.values()) {
            if (entry.getAccounts().getOrDefault(iban, null) != null) {
                entry.removeAccountCards(iban);
                entry.removeAccount(iban);
            }
        }
    }

    public void removeCard(final String cardNumber) {
        for (DatabaseEntry entry : db.values()) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                entry.removeCard(cardNumber);
            }
        }
    }

    public void addCard(final String email, final Card card) {
        DatabaseEntry entry = db.getOrDefault(email, null);
        if (entry == null) {
            return;
        }
        entry.addCard(card);
    }

    public DatabaseEntry getEntryByUser(final String email) {
        return db.getOrDefault(email, null);
    }

    public DatabaseEntry getEntryByCard(final String cardNumber) {
        for (DatabaseEntry entry : db.values()) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                return entry;
            }
        }
        return null;
    }

    public DatabaseEntry getEntryByAccount(final String input) {
        //This allows for the use of aliases instead of account iban
        String actualIban = input;
        if (aliases.containsKey(input)) {
            actualIban = aliases.get(input);
        }
        for (DatabaseEntry entry : db.values()) {
            if (entry.getAccounts().getOrDefault(actualIban, null) != null) {
                return entry;
            }
        }
        return null;
    }

    private User getUser(final String email) {
        DatabaseEntry entry = db.getOrDefault(email, null);
        if (entry == null) {
            return null;
        }
        return entry.getUser();
    }

    private Account getAccount(final String input) {
        //This allows for the use of aliases instead of account iban
        String actualIban = input;
        if (aliases.containsKey(input)) {
            actualIban = aliases.get(input);
        }
        for (DatabaseEntry entry : db.values()) {
            if (entry.getAccounts().getOrDefault(actualIban, null) != null) {
                return entry.getAccounts().get(actualIban);
            }
        }
        return null;
    }

    private Card getCard(final String cardNumber) {
        for (DatabaseEntry entry : db.values()) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                return entry.getCards().get(cardNumber);
            }
        }
        return null;
    }
}
