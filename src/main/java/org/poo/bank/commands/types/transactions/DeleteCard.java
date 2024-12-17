package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.cards.Card;
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

        Bank.getInstance().getDatabase().removeCard(card.getCardNumber());
        entry.addTransaction(card.destructionTransaction(entry, commandInput.getTimestamp()));
    }
}
