package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.fileio.CommandInput;

public class ChangeSpendingLimit extends Command {
    public ChangeSpendingLimit(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {

    }
}
