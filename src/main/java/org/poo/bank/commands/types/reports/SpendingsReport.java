package org.poo.bank.commands.types.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a spendingsReport request.
 */
public final class SpendingsReport extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public SpendingsReport(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a spendingsReport request.
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
            commandOutput.put("command", "spendingsReport");

            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Account not found");

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        if (account.isSavingsAccount()) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "spendingsReport");

            output.put("error", "This kind of report is not supported for a saving account");

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        ObjectNode spendingReport = Bank.getInstance().createObjectNode();
        spendingReport.put("command", "spendingsReport");

        ObjectNode commandOutput = Bank.getInstance().createObjectNode();
        commandOutput.put("IBAN", commandInput.getAccount());
        commandOutput.put("balance", account.getBalance());
        commandOutput.put("currency", account.getCurrency());

        entry.getUser().spendingsReportJson(commandInput.getStartTimestamp(),
                commandInput.getEndTimestamp(), commandInput.getAccount(), commandOutput);
        spendingReport.put("output", commandOutput);
        spendingReport.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(spendingReport);
    }
}
