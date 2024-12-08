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
import java.util.Map;

@Data
public class Database {
    public static Database database = new Database();

    private final ArrayList<DatabaseEntry> db;
    private Map<String, String> aliases;

    private Database() {
        db = new ArrayList<>();
        aliases = new HashMap<>();
    }

    public void reset() {
        db.clear();
        aliases.clear();
    }

    public void addAll(ObjectInput input, ObjectMapper objectMapper) {
        for (UserInput user : input.getUsers()) {
            db.add(new DatabaseEntry(new User(user, objectMapper)));
        }
    }

    public static Database getInstance() {
        return database;
    }

    public void addAlias(String alias, String iban) {
        if (getAccount(iban) != null)
            aliases.putIfAbsent(alias, iban);
    }

    public boolean addAccount(String email, Account account) {
        for (DatabaseEntry entry : db) {
            if (entry.getUser().getEmail().equals(email)) {
                entry.addAccount(account);
                return true;
            }
        }
        return false;
    }

    public boolean removeAccount(String iban) {
        for (DatabaseEntry entry : db) {
            if (entry.getAccounts().getOrDefault(iban, null) != null) {
                entry.removeAccountCards(iban);
                entry.removeAccount(iban);
                return true;
            }
        }
        return false;
    }

    public boolean removeCard(String cardNumber) {
        for (DatabaseEntry entry : db) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                entry.removeCard(cardNumber);
                return true;
            }
        }
        return false;
    }

    public boolean addCard(String email, Card card) {
        for (DatabaseEntry entry : db) {
            if (entry.getUser().getEmail().equals(email)) {
                entry.addCard(card);
                return true;
            }
        }
        return false;
    }

    public DatabaseEntry getEntryByUser(String email) {
        for (DatabaseEntry entry : db) {
            if (entry.getUser().getEmail().equals(email)) {
                return entry;
            }
        }
        return null;
    }

    public DatabaseEntry getEntryByCard(String cardNumber) {
        for (DatabaseEntry entry : db) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                return entry;
            }
        }
        return null;
    }

    public DatabaseEntry getEntryByAccount(String input) {
        //This allows for the use of aliases instead of account iban
        String actualIban = input;
        if (aliases.containsKey(input))
            actualIban = aliases.get(input);
        for (DatabaseEntry entry : db) {
            if (entry.getAccounts().getOrDefault(actualIban, null) != null) {
                return entry;
            }
        }
        return null;
    }

    public User getUser(String email) {
        for (DatabaseEntry entry : db) {
            if (entry.getUser().getEmail().equals(email)) {
                return entry.getUser();
            }
        }
        return null;
    }

    public Account getAccount(String input) {
        //This allows for the use of aliases instead of account iban
        String actualIban = input;
        if (aliases.containsKey(input))
            actualIban = aliases.get(input);
        for (DatabaseEntry entry : db) {
            if (entry.getAccounts().getOrDefault(actualIban, null) != null) {
                return entry.getAccounts().get(actualIban);
            }
        }
        return null;
    }

    public Card getCard(String cardNumber) {
        for (DatabaseEntry entry : db) {
            if (entry.getCards().getOrDefault(cardNumber, null) != null) {
                return entry.getCards().get(cardNumber);
            }
        }
        return null;
    }
}
