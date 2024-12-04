package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class DeleteAccount extends Command {
    public DeleteAccount(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getUser(commandInput.getEmail()) == null)
            return;
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;
        if (Double.compare(Database.getInstance().getAccount(commandInput.getAccount()).getBalance(), 0.0) != 0)
            return;

        Database.getInstance().removeAccount(commandInput.getAccount());
        output.put("command", "deleteAccount");
        ObjectNode node = Bank.getInstance().getObjectMapper().createObjectNode();
        node.put("success", "Account deleted");
        node.put("timestamp", commandInput.getTimestamp());
        output.put("output", node);
        output.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().getOutput().add(output);
        //TODO CONVERT OUTPUT TO JSON
    }
}
