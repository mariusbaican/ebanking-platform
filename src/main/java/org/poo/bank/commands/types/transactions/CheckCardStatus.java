package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class CheckCardStatus extends Command {
    public CheckCardStatus(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByCard(commandInput.getCardNumber());
        if (entry == null) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "checkCardStatus");
            output.put("description", "Card not found");
            output.put("timestamp", commandInput.getTimestamp());
            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        Account account = entry.getAccount(card.getIban());

        if (card.getStatus() == Card.CardStatus.ACTIVE) {
            if (account.getBalance() <= account.getMinBalance()) {
                output.put("description",
                        "You have reached the minimum amount of funds, the card will be frozen");
                output.put("timestamp", commandInput.getTimestamp());

                TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
                entry.getUser().addTransaction(data);

                card.setStatus(Card.CardStatus.FROZEN);
            }
        }
    }
}
