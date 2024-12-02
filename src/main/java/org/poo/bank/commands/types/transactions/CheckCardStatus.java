package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class CheckCardStatus extends Command {
    public CheckCardStatus(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getCard(commandInput.getCardNumber()) == null)
            return;

        Card card = Database.getInstance().getCard(commandInput.getCardNumber());
        if (card.getAccount().getBalance() <= card.getAccount().getMinBalance())
            //Frozen
            return;
        else if (card.getAccount().getBalance() - card.getAccount().getMinBalance() <= 30)
            //Warning
            return;
        //TODO CONVERT OUTPUT TO JSON
    }
}
