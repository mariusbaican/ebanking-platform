package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.AccountFactory;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class AddAccount extends Command {
    public AddAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }

        Account account = AccountFactory.build(commandInput);
        entry.addAccount(account);

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "New account created");

        TransactionData data = new TransactionData(output, account.getIban());
        entry.getUser().addTransaction(data);
    }
}
