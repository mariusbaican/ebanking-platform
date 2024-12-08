package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class CheckCardStatus extends Command {
    public CheckCardStatus(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getCard(commandInput.getCardNumber()) == null) {
            ObjectNode commandOutput = Bank.getInstance().getObjectMapper().createObjectNode();
            commandOutput.put("command", "checkCardStatus");
            output.put("description", "Card not found");
            output.put("timestamp", commandInput.getTimestamp());
            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().getOutput().add(commandOutput);
            return;
        }

        Card card = Database.getInstance().getCard(commandInput.getCardNumber());
        Account account = Database.getInstance().getAccount(card.getIban());
        if (account.getBalance() <= account.getMinBalance()) {
            output.put("description", "You have reached the minimum amount of funds, the card will be frozen");
            output.put("timestamp", commandInput.getTimestamp());
            TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
            Database.getInstance().getUser(account.getOwner()).addTransaction(data);
            card.setStatus(Card.CardStatus.FROZEN);
            return;
        } else {
            //card.setStatus(Card.CardStatus.ACTIVE);
            return;
        }
        //TODO CONVERT OUTPUT TO JSON
    }
}
