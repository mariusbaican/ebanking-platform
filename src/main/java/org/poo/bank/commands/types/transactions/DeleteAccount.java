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
        boolean foundError = false;
        if (Database.getInstance().getUser(commandInput.getEmail()) == null && !foundError)
            foundError = true;
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null && !foundError)
            foundError = true;
        if (Double.compare(Database.getInstance().getAccount(commandInput.getAccount()).getBalance(), 0.0) != 0 && !foundError)
            foundError = true;

        if (foundError) {
            ObjectNode commandOutput = Bank.getInstance().getObjectMapper().createObjectNode();
            commandOutput.put("command", "deleteAccount");
            output.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            output.put("timestamp", commandInput.getTimestamp());
            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().getOutput().add(commandOutput.deepCopy());
            return;
        }

        Database.getInstance().removeAccount(commandInput.getAccount());

        ObjectNode commandOutput = Bank.getInstance().getObjectMapper().createObjectNode();
        commandOutput.put("command", "deleteAccount");
        output.put("success", "Account deleted");
        output.put("timestamp", commandInput.getTimestamp());
        commandOutput.put("output", output);
        commandOutput.put("timestamp", commandInput.getTimestamp());

        Bank.getInstance().getOutput().add(commandOutput.deepCopy());
        //TODO CONVERT OUTPUT TO JSON
    }
}
