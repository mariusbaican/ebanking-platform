package org.poo.bank.commands.types.debug;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a printTransaction request.
 */
public final class PrintTransactions extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public PrintTransactions(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a printTransactions request.
     * It handles the error of a non-existent user being provided.
     * In case of an error, the method exits. Otherwise, it adds the
     * result to the global JSON output.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("command", "printTransactions");
        output.put("output", entry.transactionsToJson());
        output.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(output);
    }
}
