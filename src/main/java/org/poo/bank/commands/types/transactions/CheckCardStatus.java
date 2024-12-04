package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
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
        Account account = Database.getInstance().getAccount(card.getIban());
        if (account.getBalance() <= account.getMinBalance())
            //Frozen
            return;
        else if (account.getBalance() - account.getMinBalance() <= 30)
            //Warning
            return;
        //TODO CONVERT OUTPUT TO JSON
    }
}
