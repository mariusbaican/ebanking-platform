package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.currency.Currencies;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class PayOnline extends Command {
    public PayOnline(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getCard(commandInput.getCardNumber()) == null) {
            output.put("command", "payOnline");

            ObjectNode commandOutput = Bank.getInstance().getObjectMapper().createObjectNode();
            commandOutput.put("description", "Card not found");
            commandOutput.put("timestamp", commandInput.getTimestamp());

            output.put("output", commandOutput);
            output.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().getOutput().add(output.deepCopy());
            return;
        }

        Card card = Database.getInstance().getCard(commandInput.getCardNumber());
        Account account = Database.getInstance().getAccount(card.getIban());
        double adjustedPrice = commandInput.getAmount() * CurrencyExchanger.getRate(
                new Currencies<>(commandInput.getCurrency(), account.getCurrency()));

        if (card.getStatus() == Card.CardStatus.FROZEN) {
            output.put("description", "The card is frozen");
            output.put("timestamp", commandInput.getTimestamp());
            TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
            Database.getInstance().getUser(account.getOwner()).addTransaction(data);
            return;
        }

        if (account.getBalance() < adjustedPrice) {
            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Insufficient funds");
            TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
            Database.getInstance().getUser(account.getOwner()).addTransaction(data);
            return;
        }

        account.setBalance(account.getBalance() - adjustedPrice);

        if (account.getBalance() <= account.getMinBalance()) {
            output.put("description", "You have reached the minimum amount of funds, the card will be frozen");
            output.put("timestamp", commandInput.getTimestamp());

            TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
            Database.getInstance().getUser(account.getOwner()).addTransaction(data);

            card.setStatus(Card.CardStatus.FROZEN);
            return;
        }

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", "Card payment");
        output.put("amount", adjustedPrice);
        output.put("commerciant", commandInput.getCommerciant());

        TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
        Database.getInstance().getUser(account.getOwner()).addTransaction(data);

        if (card.isOneTime())
            Database.getInstance().removeCard(commandInput.getCardNumber());
    }


}
