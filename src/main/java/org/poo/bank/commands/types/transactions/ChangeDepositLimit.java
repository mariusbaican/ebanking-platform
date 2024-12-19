package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.fileio.CommandInput;

public class ChangeDepositLimit extends Command {
    public ChangeDepositLimit(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {

    }
}
