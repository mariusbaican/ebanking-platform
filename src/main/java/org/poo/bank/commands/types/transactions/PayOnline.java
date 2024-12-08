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

public final class PayOnline extends Command {
    public PayOnline(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByCard(commandInput.getCardNumber());
        if (entry == null) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "payOnline");

            output.put("description", "Card not found");
            output.put("timestamp", commandInput.getTimestamp());

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        Account account = entry.getAccount(card.getIban());

        if (card.getStatus() == Card.CardStatus.FROZEN) {
            output.put("description", "The card is frozen");
            output.put("timestamp", commandInput.getTimestamp());

            TransactionData data = new TransactionData(output, account.getIban());
            entry.getUser().addTransaction(data);

            return;
        }

        double sumPaid = account.pay(commandInput.getAmount(), commandInput.getCurrency());
        if (Double.compare(sumPaid, 0.0) == 0) {
            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Insufficient funds");

            TransactionData data = new TransactionData(output, account.getIban());
            entry.getUser().addTransaction(data);

            return;
        }

        if (account.getBalance() <= account.getMinBalance()) {
            output.put("description",
                    "You have reached the minimum amount of funds, the card will be frozen");
            output.put("timestamp", commandInput.getTimestamp());

            TransactionData data = new TransactionData(output, account.getIban());
            entry.getUser().addTransaction(data);

            card.setStatus(Card.CardStatus.FROZEN);

            return;
        }

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "Card payment");
        output.put("amount", sumPaid);
        output.put("commerciant", commandInput.getCommerciant());

        TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
        entry.getUser().addTransaction(data);

        if (card.isOneTime()) {
            TransactionData deleteCard =
                    new TransactionData(
                            card.destroyOutput(commandInput.getTimestamp()),
                            card.getIban());
            entry.getUser().addTransaction(deleteCard);
            Database.getInstance().removeCard(commandInput.getCardNumber());

            Card newCard = new Card(card.getIban(), true);
            entry.addCard(newCard);
            TransactionData createCard =
                    new TransactionData(
                            newCard.createOutput(commandInput.getTimestamp()),
                            newCard.getIban());
            entry.getUser().addTransaction(createCard);
        }
    }


}
