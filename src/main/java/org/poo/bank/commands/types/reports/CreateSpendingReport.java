package org.poo.bank.commands.types.reports;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.bank.output.logs.reports.Report;
import org.poo.bank.output.logs.reports.ReportFactory;
import org.poo.bank.output.visitor.JsonVisitor;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a spendingsReport request.
 */
public final class CreateSpendingReport extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CreateSpendingReport(final CommandInput commandInput) {
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
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                            .addField("timestamp", Bank.getInstance().getTimestamp())
                            .addField("description", "Account not found")
                            .asObjectNode()
                    )
                    .asObjectNode()
            );
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        if (account.getAccountType() == Account.AccountType.SAVINGS) {
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                            .addField("error", "This kind of report is not supported for a saving account")
                            .asObjectNode()
                    )
                    .asObjectNode()
            );
            return;
        }

        Bank.getInstance().addToOutput(new Response()
                .addField("command", commandInput.getCommand())
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("output",
                        ReportFactory.createReport(Report.ReportType.SPENDING, commandInput.getAccount())
                                .accept(new JsonVisitor()))
                .asObjectNode()
        );
    }
}
