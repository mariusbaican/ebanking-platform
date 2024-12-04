package org.poo.bank.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public abstract class Command {
    protected CommandInput commandInput;
    protected ObjectMapper mapper;
    protected ObjectNode output;

    public Command(CommandInput commandInput) {
        this.commandInput = commandInput;
        this.mapper = Bank.getInstance().getObjectMapper();
        output = mapper.createObjectNode();
    }

    public abstract void run();

    public final ObjectNode toJson() {
        return output;
    }
}
