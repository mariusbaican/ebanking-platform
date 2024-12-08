package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class DeleteCard extends Command {
    public DeleteCard(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByCard(commandInput.getCardNumber());
        if (entry == null) {
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        Account account = entry.getAccount(card.getIban());

        entry.removeCard(card.getCardNumber());

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "The card has been destroyed");
        output.put("card", commandInput.getCardNumber());
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", account.getIban());

        TransactionData data = new TransactionData(output, account.getIban());
        entry.getUser().addTransaction(data);
    }
}
