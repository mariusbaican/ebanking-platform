package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public class DeleteCard extends Command {
    public DeleteCard(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getCard(commandInput.getCardNumber()) == null)
            return;

        DatabaseEntry entry = Database.getInstance().getEntryByCard(commandInput.getCardNumber());
        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "The card has been destroyed");
        output.put("card", commandInput.getCardNumber());
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", entry.getCards().get(commandInput.getCardNumber()).getIban());
        TransactionData data = new TransactionData(output.deepCopy(), entry.getCards().get(commandInput.getCardNumber()).getIban());
        Database.getInstance().getUser(entry.getUser().getEmail()).addTransaction(data);
        Database.getInstance().removeCard(commandInput.getCardNumber());
    }
}
