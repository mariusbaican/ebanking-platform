package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class DeleteAccount extends Command {
    public DeleteAccount(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        if (Database.getInstance().getAccount(commandInput.getAccount()).getBalance() == 0) {
            Account removedAccount = Database.getInstance().removeAccount(commandInput.getAccount());
            for (String alias : removedAccount.getAliases())
                Database.getInstance().removeAccount(alias);
            for (Card card : removedAccount.getCards())
                Database.getInstance().removeCard(card.getCardNumber());
        } else {

        }
        //TODO CONVERT OUTPUT TO JSON
    }
}
