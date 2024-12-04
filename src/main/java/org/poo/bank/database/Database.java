package org.poo.bank.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.User;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Data
public class Database {
    public static Database database = new Database();

    private final ArrayList<DatabaseEntry> db;

    private Database() {
        db = new ArrayList<>();
    }

    public void reset() {
        db.clear();
    }

    public void addAll(ObjectInput input, ObjectMapper objectMapper) {
        for (UserInput user : input.getUsers()) {
            db.add(new DatabaseEntry(new User(user, objectMapper)));
        }
    }

    public static Database getInstance() {
        return database;
    }

    public boolean addAccount(String email, Account account) {
        for (DatabaseEntry entry : db) {
            if (entry.getUser().getEmail().equals(email)) {
                entry.addAccount(account);
                System.out.println(account.getIban() + "added to database");
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

    public DatabaseEntry getEntryByAccount(String accountNumber) {
        for (DatabaseEntry entry : db) {
            if (entry.getAccounts().getOrDefault(accountNumber, null) != null) {
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

    public Account getAccount(String iban) {
        for (DatabaseEntry entry : db) {
            if (entry.getAccounts().getOrDefault(iban, null) != null) {
                return entry.getAccounts().get(iban);
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
