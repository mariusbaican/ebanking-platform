package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing an addInterest request
 */
public final class AddInterest extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public AddInterest(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute an addInterest request.
     * It handles the error of a non-existing account being provided
     * or a classicAccount being provided.
     * If the account is invalid, the method exits. If the account is
     * not a savingsAccount the result is added to the global JSON
     * output.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        boolean ret = account.addInterest();
        if (!ret) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", commandInput.getCommand());
            commandOutput.put("output",
                    account.addInterestJson(commandInput.getTimestamp()));
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
        }
    }
}
