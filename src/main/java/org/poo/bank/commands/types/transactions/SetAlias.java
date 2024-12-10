package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a setAlias request
 */
public final class SetAlias extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public SetAlias(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a setAlias request.
     * If a non-existent account is provided, the method exits.
     * Otherwise, the alias is added to the database.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Bank.getInstance().getDatabase()
                .addAlias(commandInput.getAccount(), commandInput.getAlias());
    }
}
