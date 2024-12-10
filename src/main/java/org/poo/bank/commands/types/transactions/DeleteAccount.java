package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
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
                output.put("description",
                        "Account couldn't be deleted - there are funds remaining");
                output.put("timestamp", commandInput.getTimestamp());

                entry.getUser().addTransaction(
                        new TransactionData(output, commandInput.getAccount()));
                foundError = true;
            }
        }

        if (foundError) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "deleteAccount");

            ObjectNode error = Bank.getInstance().createObjectNode();
            error.put("error",
                    "Account couldn't be deleted - see org.poo.transactions for details");
            error.put("timestamp", commandInput.getTimestamp());

            commandOutput.put("output", error);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput.deepCopy());

            return;
        }

        entry.removeAccount(commandInput.getAccount());

        ObjectNode commandOutput = Bank.getInstance().createObjectNode();
        commandOutput.put("command", "deleteAccount");

        output.put("success", "Account deleted");
        output.put("timestamp", commandInput.getTimestamp());

        commandOutput.put("output", output);
        commandOutput.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(commandOutput.deepCopy());
    }
}
