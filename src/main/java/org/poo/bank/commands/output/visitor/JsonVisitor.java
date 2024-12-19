package org.poo.bank.commands.output.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;

public class JsonVisitor implements OutputVisitor<ObjectNode> {

    @Override
    public ObjectNode convertDatabase(Database database) {
        ObjectNode databaseJson = Bank.getInstance().createObjectNode();
        ArrayNode entriesArray = Bank.getInstance().createArrayNode();
        for (DatabaseEntry entry : Bank.getInstance().getDatabase().getEntries()) {
            entriesArray.add(convertEntry(entry));
        }
        databaseJson.put("output", entriesArray);
        return databaseJson;
    }

    @Override
    public ObjectNode convertEntry(DatabaseEntry entry) {
        ObjectNode entryJson = convertUser(entry.getUser());
        ArrayNode accountsJson = Bank.getInstance().createArrayNode();
        for (Account account : entry.getAccounts().values()) {
            accountsJson.add(convertAccount(account));
        }
        entryJson.put("accounts", accountsJson);
        return entryJson;
    }

    @Override
    public ObjectNode convertUser(User user) {
        ObjectNode userJson = Bank.getInstance().createObjectNode();
        userJson.put("firstName", user.getFirstName());
        userJson.put("lastName", user.getLastName());
        userJson.put("email", user.getEmail());
        userJson.put("birthDate", user.getBirthDate());
        userJson.put("occupation", user.getOccupation());
        return userJson;
    }

    @Override
    public ObjectNode convertAccount(Account account) {
        ObjectNode accountJson = Bank.getInstance().createObjectNode();
        accountJson.put("IBAN", account.getIban());
        accountJson.put("balance", account.getBalance());
        accountJson.put("currency", account.getCurrency());
        accountJson.put("type", account.getAccountType());
        accountJson.put("servicePlan", account.getServicePlan().toString());
        ArrayNode cardsJson = Bank.getInstance().createArrayNode();

        DatabaseEntry entry
                = Bank.getInstance().getDatabase().getEntryByAccount(account.getIban());
        if (entry == null)
            return null;
        for (Card card : entry.getCards().values()) {
            if (card.getIban().equals(account.getIban())) {
                cardsJson.add(convertCard(card));
            }
        }
        accountJson.put("cards", cardsJson);
        return accountJson;
    }

    @Override
    public ObjectNode convertCard(Card card) {
        ObjectNode cardJson = Bank.getInstance().createObjectNode();
        cardJson.put("cardNumber", card.getCardNumber());
        cardJson.put("status", card.getStatus().toString());
        return cardJson;
    }
}
