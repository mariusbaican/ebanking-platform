package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class ChangeInterestRate extends Command {
    public ChangeInterestRate(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        boolean ret = account.setInterest(commandInput.getInterestRate());
        if (!ret) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "changeInterestRate");

            output.put("description", "This is not a savings account");
            output.put("timestamp", commandInput.getTimestamp());

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
        } else {
            output.put("description",
                    "Interest rate of the account changed to " + commandInput.getInterestRate());
            output.put("timestamp", commandInput.getTimestamp());

            TransactionData data = new TransactionData(output, commandInput.getAccount());
            entry.getUser().addTransaction(data);
        }
    }
}
