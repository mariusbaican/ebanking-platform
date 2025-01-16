package org.poo.bank.output.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.TransactionData;
import org.poo.bank.output.logs.TransactionHistory;
import org.poo.bank.output.logs.reports.AccountReport;
import org.poo.bank.output.logs.reports.BusinessReport;
import org.poo.bank.output.logs.reports.SpendingReport;

public class JsonVisitor implements OutputVisitor<ObjectNode> {
    @Override
    public ObjectNode convertDatabase(Database database) {
        ObjectNode databaseJson = Bank.getInstance().createObjectNode();
        ArrayNode entriesArray = Bank.getInstance().createArrayNode();
        for (DatabaseEntry entry : Bank.getInstance().getDatabase().getEntries()) {
            entriesArray.add(convertEntry(entry));
        }
        databaseJson.putPOJO("output", entriesArray);
        return databaseJson;
    }

    @Override
    public ObjectNode convertEntry(DatabaseEntry entry) {
        ObjectNode entryJson = convertUser(entry.getUser());
        ArrayNode accountsJson = Bank.getInstance().createArrayNode();
        for (Account account : entry.getAccounts().values()) {
            accountsJson.add(convertAccount(account));
        }
        entryJson.putPOJO("accounts", accountsJson);
        return entryJson;
    }

    @Override
    public ObjectNode convertUser(User user) {
        ObjectNode userJson = Bank.getInstance().createObjectNode();
        userJson.put("firstName", user.getFirstName());
        userJson.put("lastName", user.getLastName());
        userJson.put("email", user.getEmail());
        return userJson;
    }

    @Override
    public ObjectNode convertAccount(Account account) {
        ObjectNode accountJson = Bank.getInstance().createObjectNode();
        accountJson.put("IBAN", account.getIban());
        accountJson.put("balance", account.getBalance());
        accountJson.put("currency", account.getCurrency());
        accountJson.put("type", account.getAccountType().toString());
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

    @Override
    public ObjectNode convertTransactionHistory(TransactionHistory transactionHistory) {
        ObjectNode transactionHistoryJson = Bank.getInstance().createObjectNode();
        ArrayNode transactionsJson = Bank.getInstance().createArrayNode();
        for (TransactionData data : transactionHistory.getTransactions()) {
            transactionsJson.add(data.toJson());
        }
        transactionHistoryJson.putPOJO("transactions", transactionsJson);
        return transactionHistoryJson;
    }

    @Override
    public ObjectNode convertAccountReport(AccountReport accountReport) {
        ObjectNode accountReportJson = Bank.getInstance().createObjectNode();
        accountReportJson.put("IBAN", accountReport.getAccount().getIban());
        accountReportJson.put("balance", accountReport.getAccount().getBalance());
        accountReportJson.put("currency", accountReport.getAccount().getCurrency());
        accountReportJson.putPOJO("transactions",
                accountReport.getTransactionHistory().accept(this));
        return accountReportJson;
    }

    @Override
    public ObjectNode convertSpendingReport(SpendingReport spendingReport) {
        ObjectNode spendingReportJson = Bank.getInstance().createObjectNode();
        return spendingReportJson;
    }

    @Override
    public ObjectNode convertBusinessReport(BusinessReport businessReport) {
        return null;
    }
}
