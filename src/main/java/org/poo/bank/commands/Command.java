package org.poo.bank.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

/**
 * This is an abstract Command class with the purpose of setting the standard for any command type.
 * It implements the Command design pattern.
 */
public abstract class Command {
    protected final CommandInput commandInput;
    protected final ObjectNode output; //TODO REDO OUTPUT ARCHITECTURE

    /**
     * This constructor stores the commandInput data and initializes the output node.
     * @param commandInput The input for the requested command.
     */
    public Command(final CommandInput commandInput) {
        this.commandInput = commandInput;
        output = Bank.getInstance().createObjectNode();
    }

    /**
     * This abstract method is used to execute the command.
     */
    public abstract void run();
}
