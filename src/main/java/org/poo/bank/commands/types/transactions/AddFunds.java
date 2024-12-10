package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing an addFunds request
 */
public final class AddFunds extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public AddFunds(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute an addFunds request.
     * It handles the error of a non-existent account being provided.
     * In case of an error, the method exits.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        account.receive(commandInput.getAmount(), account.getCurrency());
    }
}
