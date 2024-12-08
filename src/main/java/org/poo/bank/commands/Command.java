package org.poo.bank.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public abstract class Command {
    protected final CommandInput commandInput;
    protected final ObjectNode output;

    public Command(final CommandInput commandInput) {
        this.commandInput = commandInput;
        output = Bank.getInstance().createObjectNode();
    }

    public abstract void run();

    public final ObjectNode toJson() {
        return output;
    }
}
