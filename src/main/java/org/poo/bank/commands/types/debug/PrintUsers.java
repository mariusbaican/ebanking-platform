package org.poo.bank.commands.types.debug;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.output.logs.Response;
import org.poo.bank.output.visitor.JsonVisitor;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a printUsers request.
 */
public final class PrintUsers extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public PrintUsers(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a printUsers request.
     * It adds the result to the global JSON output.
     */
    @Override
    public void run() {
        Bank.getInstance().addToOutput(new Response()
                .addField("command", commandInput.getCommand())
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("output", Bank.getInstance().getDatabase().accept(new JsonVisitor()).get("output"))
                .asObjectNode()
        );
    }
}
