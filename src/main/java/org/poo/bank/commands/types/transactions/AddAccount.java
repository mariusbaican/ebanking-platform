package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.AccountFactory;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing an addAccount request
 */
public final class AddAccount extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public AddAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute an addAccount request.
     * It handles the error of a non-existent user being provided.
     * In case of an error, the method exits. Otherwise, the result
     * is added to the user's transactionHistory.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }

        Account account = AccountFactory.build(commandInput);
        entry.addAccount(account);

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "New account created");

        entry.getUser().addTransaction(new TransactionData(output, account.getIban()));
    }
}
