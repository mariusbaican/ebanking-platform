package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.AccountBuilder;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class AddAccount extends Command {
    public AddAccount(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getUser(commandInput.getEmail()) == null)
            return;

        Account account = AccountBuilder.build(commandInput);
        Database.getInstance().addAccount(commandInput.getEmail(), account);
//        System.out.println(account.getIban());
//        System.out.println(commandInput.getEmail());
    }
}
