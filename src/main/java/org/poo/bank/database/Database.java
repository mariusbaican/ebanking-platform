package org.poo.bank.database;

import org.poo.bank.Bank;
import org.poo.bank.Currencies;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.Card;
import org.poo.bank.components.User;

import java.util.HashMap;

public class Database {
    public static Database database = new Database();

    private HashMap<String, User> users;
    private HashMap<String, Account> accounts;
    private HashMap<String, Card> cards;
    private HashMap<Currencies<String, String>, Double> exchangeRates;

    private Database() {
        users = new HashMap<>();
        accounts = new HashMap<>();
        cards = new HashMap<>();
    }

    public void reset() {
        users.clear();
        accounts.clear();
        cards.clear();
    }

    public static Database getInstance() {
        return database;
    }

    public void addUser(String email, User user) {
        users.put(email, user);
    }

    public User getUser(String email) {
        return users.get(email);
    }

    public User removeUser(String email) {
        return users.remove(email);
    }

    public void addAccount(String iban, Account account) {
        accounts.put(iban, account);
    }

    public Account getAccount(String iban) {
        return accounts.get(iban);
    }

    public Account removeAccount(String iban) {
        return accounts.remove(iban);
    }

    public void addCard(String cardNumer, Card card) {
        cards.put(cardNumer, card);
    }

    public Card getCard(String cardNumer) {
        return cards.get(cardNumer);
    }

    public Card removeCard(String cardNumer) {
        return cards.remove(cardNumer);
    }

    public void addExchangeRate(Currencies<String,String> currencies, Double exchangeRate) {
        exchangeRates.put(currencies, exchangeRate);
    }

    public double getExchangeRate(Currencies<String,String> currencies) {
        return exchangeRates.get(currencies);
    }

    public double removeExchangeRate(Currencies<String,String> currencies) {
        return exchangeRates.remove(currencies);
    }
}
