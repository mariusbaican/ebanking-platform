package org.poo.bank.output.visitor;

import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.TransactionHistory;
import org.poo.bank.output.logs.reports.AccountReport;
import org.poo.bank.output.logs.reports.BusinessReport;
import org.poo.bank.output.logs.reports.SpendingReport;

public interface OutputVisitor<T> {
    T convertDatabase(Database database);
    T convertEntry(DatabaseEntry entry);
    T convertUser(User user);
    T convertAccount(Account account);
    T convertCard(Card card);
    T convertTransactionHistory(TransactionHistory transactionHistory);
    T convertAccountReport(AccountReport accountReport);
    T convertSpendingReport(SpendingReport spendingReport);
    T convertBusinessReport(BusinessReport businessReport);
}
