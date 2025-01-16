package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a deleteAccount request
 */
public final class DeleteAccount extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public DeleteAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a deleteAccount request.
     * It handles the error of a non-existent user or account being provided.
     * If any error occurs, a message is written to the global output.
     * If the user or account are invalid, the method exits. If the account
     * balance is not 0, a transaction is added to the user's transaction
     * history. Otherwise, a confirmation transaction gets added to the global output.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByUser(commandInput.getEmail());
        Account account;
        boolean foundError = (entry == null);
        if (!foundError) {
            account = entry.getAccount(commandInput.getAccount());
            if (account == null) {
                foundError = true;
            } else if (Double.compare(account.getBalance(), 0.0) != 0) {
                entry.addTransaction(new Response()
                        .addField("timestamp", Bank.getInstance().getTimestamp())
                        .addField("description", "Account couldn't be deleted - there are funds remaining")
                        .asTransactionData(account.getIban())
                );
                foundError = true;
            }
        }

        if (foundError) {
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                            .addField("timestamp", Bank.getInstance().getTimestamp())
                            .addField("error", "Account couldn't be deleted - see org.poo.transactions for details")
                            .asObjectNode()
                    )
                    .asObjectNode()
            );
            return;
        }

        Bank.getInstance().getDatabase().removeAccount(commandInput.getAccount());
        Bank.getInstance().addToOutput(new Response()
                .addField("command", commandInput.getCommand())
                .addField("timestamp", commandInput.getTimestamp())
                .addField("output", new Response()
                        .addField("timestamp", commandInput.getTimestamp())
                        .addField("success", "Account deleted")
                        .asObjectNode()
                )
                .asObjectNode()
        );
    }
}
