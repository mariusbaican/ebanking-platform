package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
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

        Database.getInstance().addCard(commandInput.getEmail(), new Card(commandInput, isOneTime));
        //TODO STORE OUTPUT IN OBJECTNODE
    }
}
