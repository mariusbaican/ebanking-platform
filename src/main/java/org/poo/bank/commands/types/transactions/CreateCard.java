package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.CardFactory;
import org.poo.bank.components.cards.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a createCard request
 */
public final class CreateCard extends Command {

    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CreateCard(final CommandInput commandInput) {
        super(commandInput);
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
        // I found that I accidentally resolved the edge case of a user trying to create a card
        // for another user. I firstly pull up the requesting user's entry and then look for the
        // target account in his entry, thus if he doesn't own it, the request doesn't go through.
        Account account = entry.getAccount(commandInput.getAccount());
        if (account == null) {
            return;
        }

        Card card = CardFactory.generate(commandInput, Card.CardType.REGULAR);
        Bank.getInstance().getDatabase().addCard(commandInput.getEmail(), card);

        entry.addTransaction(new Response()
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("description", "New card created")
                .addField("card", card.getCardNumber())
                .addField("cardHolder", entry.getUser().getEmail())
                .addField("account", account.getIban())
                .asTransactionData(account.getIban())
        );
    }
}
