package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import org.poo.bank.commands.CommandBuilder;
import org.poo.bank.database.Database;
import org.poo.fileio.*;
import org.poo.utils.Utils;

@Data
public class Bank {
    private static Bank bank = new Bank();
    private ObjectMapper objectMapper;
    private ArrayNode output;

    private Bank() { }

    public static Bank getInstance() {
        return bank;
    }

    public void runTest(ObjectInput input, ObjectMapper objectMapper, ArrayNode output) {
        this.objectMapper = objectMapper;
        this.output = output;
        Utils.resetRandom();
        Database.getInstance().reset();
        Database.getInstance().addAll(input, objectMapper);
        for (CommandInput commandInput : input.getCommands()) {
            if (CommandBuilder.build(commandInput) != null)
                CommandBuilder.build(commandInput).run();
        }
    }
}
