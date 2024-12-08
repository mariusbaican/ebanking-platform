package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class AddFunds extends Command {
    public AddFunds(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        Account targetAccount = Database.getInstance().getAccount(commandInput.getAccount());
        targetAccount.setBalance(targetAccount.getBalance() + commandInput.getAmount());
        //TODO STORE OUTPUT IN OBJECTNODE
    }
}
