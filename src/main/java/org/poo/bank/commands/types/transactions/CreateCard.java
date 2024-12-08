package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class CreateCard extends Command {
    private final boolean isOneTime;

    public CreateCard(final CommandInput commandInput) {
        super(commandInput);
        isOneTime = false;
    }

    public CreateCard(final CommandInput commandInput, final boolean isOneTime) {
        super(commandInput);
        this.isOneTime = isOneTime;
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }
        Account account = entry.getAccount(commandInput.getAccount());
        if (account == null) {
            return;
        }

        Card card = new Card(commandInput, isOneTime);
        entry.addCard(card);

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "New card created");
        output.put("card", card.getCardNumber());
        output.put("cardHolder", commandInput.getEmail());
        output.put("account", commandInput.getAccount());

        TransactionData data = new TransactionData(output.deepCopy(), commandInput.getAccount());
        entry.getUser().addTransaction(data);
    }
}
