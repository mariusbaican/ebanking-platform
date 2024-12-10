package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a deleteCard request
 */
public final class DeleteCard extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public DeleteCard(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a deleteCard request.
     * It handles the error of a non-existent card being provided.
     * If the card is invalid, the method exits. Otherwise, a confirmation
     * transaction is added to the user's transaction history.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByCard(commandInput.getCardNumber());
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

        entry.getUser().addTransaction(new TransactionData(output, account.getIban()));
    }
}
