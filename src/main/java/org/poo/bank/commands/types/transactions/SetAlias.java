package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class SetAlias extends Command {
    public SetAlias(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        Database.getInstance().addAlias(commandInput.getAccount(), commandInput.getAlias());
    }
}
