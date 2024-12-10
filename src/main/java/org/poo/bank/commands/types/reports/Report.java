package org.poo.bank.commands.types.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a report request
 */
public final class Report extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public Report(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a Report request.
     * It handles the error of a non-existent account being provided.
     * With or without an error occurring, it adds the result to the
     * global JSON output.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "report");

            output.put("description", "Account not found");
            output.put("timestamp", commandInput.getTimestamp());

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        ObjectNode report = Bank.getInstance().createObjectNode();
        report.put("command", "report");

        ObjectNode accountInfo = Bank.getInstance().createObjectNode();
        accountInfo.put("IBAN", commandInput.getAccount());
        accountInfo.put("balance",
                entry.getAccounts().get(commandInput.getAccount()).getBalance());
        accountInfo.put("currency",
                entry.getAccounts().get(commandInput.getAccount()).getCurrency());
        accountInfo.put("transactions",
                entry.getUser().transactionsToJson(commandInput.getStartTimestamp(),
                        commandInput.getEndTimestamp(), commandInput.getAccount()));

        report.put("output", accountInfo);
        report.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(report);
    }
}
