package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.cards.Card;
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
            commandOutput.put("command", commandInput.getCommand());

            commandOutput.put("output",
                    Bank.getInstance().cardNotFoundJson(commandInput.getTimestamp()));
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        card.pay(entry, commandInput.getAmount(), commandInput.getCurrency(),
                commandInput.getTimestamp(), commandInput.getCommerciant());

    }


}
