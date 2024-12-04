package org.poo.bank.commands.types.debug;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public class PrintUsers extends Command {
    public PrintUsers(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        output.put("command", "printUsers");
        ArrayNode userArray = Bank.getInstance().getObjectMapper().createArrayNode();
        for (DatabaseEntry entry : Database.getInstance().getDb()) {
            ObjectNode userNode = entry.toJson();
            userArray.add(userNode);
        }
        output.put("output", userArray);
        output.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().getOutput().add(output);
    }
}
