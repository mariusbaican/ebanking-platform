package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class SetMinBalance extends Command {
    public SetMinBalance(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        Account account = Database.getInstance().getAccount(commandInput.getAccount());
        if (!Bank.getInstance().getActiveUser().getEmail().equals(account.getOwner().getEmail()))
            return;

        account.setMinBalance(commandInput.getMinBalance());
        //TODO CONVERT OUTPUT TO JSON
    }
}
