package org.poo.bank.commands.output.visitor;

import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;

public interface OutputVisitor<T> {
    T convertDatabase(Database database);
    T convertEntry(DatabaseEntry entry);
    T convertUser(User user);
    T convertAccount(Account account);
    T convertCard(Card card);
}
