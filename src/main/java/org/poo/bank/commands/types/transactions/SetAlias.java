package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class SetAlias extends Command {
    public SetAlias(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Database.getInstance().addAlias(commandInput.getAccount(), commandInput.getAlias());
    }
}
