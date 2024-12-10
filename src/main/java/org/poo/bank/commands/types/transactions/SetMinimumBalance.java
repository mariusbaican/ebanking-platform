package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a setMinimumBalance request
 */
public final class SetMinimumBalance extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public SetMinimumBalance(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a setMinimumBalance request.
     * If the account is non-existent, the method exits. Otherwise,
     * the account's minBalance is changed.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        entry.getAccount(commandInput.getAccount()).setMinBalance(commandInput.getAmount());
    }
}
