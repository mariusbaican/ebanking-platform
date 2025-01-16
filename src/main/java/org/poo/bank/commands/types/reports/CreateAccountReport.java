package org.poo.bank.commands.types.reports;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.bank.output.logs.reports.Report;
import org.poo.bank.output.logs.reports.ReportFactory;
import org.poo.bank.output.visitor.JsonVisitor;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a report request
 */
public final class CreateAccountReport extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CreateAccountReport(final CommandInput commandInput) {
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
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                                    .addField("description", "Account not found")
                                    .addField("timestamp", commandInput.getTimestamp())
                                    .asObjectNode()
                            )
                    .asObjectNode()
            );
            return;
        }

        Bank.getInstance().addToOutput(
                ReportFactory.createReport(Report.ReportType.ACCOUNT, commandInput.getAccount())
                        .setMinTimestamp(commandInput.getStartTimestamp())
                        .setMaxTimestamp(commandInput.getEndTimestamp())
                        .accept(new JsonVisitor())
        );
    }
}
