package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class DeleteCard extends Command {
    public DeleteCard(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getCard(commandInput.getCardNumber()) == null)
            return;

        Database.getInstance().removeCard(commandInput.getCardNumber());
        //TODO CONVERT OUTPUT TO JSON
    }
}
