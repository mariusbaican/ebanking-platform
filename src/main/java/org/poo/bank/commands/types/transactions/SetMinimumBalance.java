package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class SetMinimumBalance extends Command {
    public SetMinimumBalance(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        entry.getAccount(commandInput.getAccount()).setMinBalance(commandInput.getAmount());
    }
}
