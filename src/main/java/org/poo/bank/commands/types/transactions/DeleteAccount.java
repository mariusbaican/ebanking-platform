package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class DeleteAccount extends Command {
    public DeleteAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByUser(commandInput.getEmail());
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

                TransactionData data = new TransactionData(output, commandInput.getAccount());
                entry.getUser().addTransaction(data);
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
