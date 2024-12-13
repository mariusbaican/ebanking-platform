package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.CardFactory;
import org.poo.bank.components.cards.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a createCard request
 */
public final class CreateCard extends Command {
    private final boolean isOneTime;

    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CreateCard(final CommandInput commandInput) {
        super(commandInput);
        isOneTime = false;
    }

    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CreateCard(final CommandInput commandInput, final boolean isOneTime) {
        super(commandInput);
        this.isOneTime = isOneTime;
    }

    /**
     * This method is used to execute a createCard request.
     * It handles the error of a non-existent user or account being provided.
     * If the user or account are invalid, the method exits. Otherwise, a
     * confirmation transaction is added to the user's transaction history.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }
        Account account = entry.getAccount(commandInput.getAccount());
        if (account == null) {
            return;
        }

        Card card = CardFactory.build(commandInput, isOneTime);
        entry.addCard(card);

        entry.addTransaction(card.creationTransaction(entry, commandInput.getTimestamp()));
    }
}
