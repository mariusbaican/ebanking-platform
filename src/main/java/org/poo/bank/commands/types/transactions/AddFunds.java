package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class AddFunds extends Command {
    public AddFunds(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        account.receive(commandInput.getAmount(), account.getCurrency());
    }
}
