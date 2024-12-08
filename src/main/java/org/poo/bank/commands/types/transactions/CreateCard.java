package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class CreateCard extends Command {
    private boolean isOneTime;

    public CreateCard(CommandInput commandInput) {
        super(commandInput);
        isOneTime = false;
    }

    public CreateCard(CommandInput commandInput, boolean isOneTime) {
        super(commandInput);
        this.isOneTime = isOneTime;
    }

    @Override
    public void run() {
        if (Database.getInstance().getUser(commandInput.getEmail()) == null)
            return;

        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        Card card = new Card(commandInput, isOneTime);
        Database.getInstance().addCard(commandInput.getEmail(), card);
        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "New card created");
        output.put("card", card.getCardNumber());
        output.put("cardHolder", commandInput.getEmail());
        output.put("account", commandInput.getAccount());
        TransactionData data = new TransactionData(output.deepCopy(), commandInput.getAccount());
        Database.getInstance().getUser(commandInput.getEmail()).addTransaction(data);
    }
}
