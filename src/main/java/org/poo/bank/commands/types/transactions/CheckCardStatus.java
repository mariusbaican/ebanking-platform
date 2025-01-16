package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.cards.Card;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a checkCardStatus request
 */
public final class CheckCardStatus extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public CheckCardStatus(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a CheckCardStatus request.
     * It handles the error of a non-existent card being provided.
     * If the account is invalid, an error is added to the global output.
     * If the card is active, but the balance is below the minimum, it gets frozen
     * and a transaction is added to the user's transaction history.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByCard(commandInput.getCardNumber());
        if (entry == null) {
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                            .addField("timestamp", Bank.getInstance().getTimestamp())
                            .addField("description", "Card not found")
                            .asObjectNode()
                    )
                    .asObjectNode()
            );
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        Account account = entry.getAccount(card.getIban());
        if (card.getStatus() == Card.CardStatus.ACTIVE) {
            if (account.getBalance() <= account.getMinBalance()) {
                entry.addTransaction(new Response()
                        .addField("timestamp", Bank.getInstance().getTimestamp())
                        .addField("description",
                                "You have reached the minimum amount of funds, the card will be frozen")
                        .asTransactionData(account.getIban())
                );
            }
        }
    }
}
