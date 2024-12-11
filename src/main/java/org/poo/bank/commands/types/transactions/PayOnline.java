package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a payOnline request
 */
public final class PayOnline extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public PayOnline(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a payOnline request.
     * In case of a non-existent card, an error message is added
     * to the global output. If the card cannot afford the payment or
     * is frozen, an error message gets added to the user's transaction list.
     * If the card reaches a balance below the minimum, a warning message
     * is added to the user's transaction list. If successful, a confirmation
     * is added to the user's transaction history. If the card is a one time card
     * it gets replaced in the database by a new one, adding the required transaction
     * messages.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByCard(commandInput.getCardNumber());
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

            entry.addTransaction(new TransactionData(output, account.getIban()));
            return;
        }

        double sumPaid = account.pay(commandInput.getAmount(), commandInput.getCurrency());
        if (Double.compare(sumPaid, 0.0) == 0) {
            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Insufficient funds");

            entry.addTransaction(new TransactionData(output, account.getIban()));
            return;
        }

        if (account.getBalance() <= account.getMinBalance()) {
            output.put("description",
                    "You have reached the minimum amount of funds, the card will be frozen");
            output.put("timestamp", commandInput.getTimestamp());

            entry.addTransaction(new TransactionData(output, account.getIban()));
            card.setStatus(Card.CardStatus.FROZEN);
            return;
        }

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "Card payment");
        output.put("amount", sumPaid);
        output.put("commerciant", commandInput.getCommerciant());

        entry.addTransaction(new TransactionData(output.deepCopy(), account.getIban()));

        if (card.isOneTime()) {
            entry.addTransaction(new TransactionData(
                    card.destructionOutput(commandInput.getTimestamp()),
                    card.getIban()));
            Bank.getInstance().getDatabase().removeCard(commandInput.getCardNumber());

            Card newCard = new Card(card.getIban(), true);
            entry.addCard(newCard);
            entry.addTransaction(new TransactionData(
                    newCard.creationOutput(commandInput.getTimestamp()),
                    newCard.getIban()));
        }
    }


}
