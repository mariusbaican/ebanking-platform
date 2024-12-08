package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.currency.Currencies;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class SendMoney extends Command {
    public SendMoney(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getUser(commandInput.getEmail()) == null)
            return;

        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;

        if (Database.getInstance().getAccount(commandInput.getReceiver()) == null)
            return;

        Account sender = Database.getInstance().getAccount(commandInput.getAccount());
        if (!sender.getOwner().equals(commandInput.getEmail()))
            return;

        if (Double.compare(sender.getBalance(), commandInput.getAmount()) < 0) {
            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Insufficient funds");
            TransactionData data = new TransactionData(output.deepCopy(), sender.getIban());
            Database.getInstance().getUser(sender.getOwner()).addTransaction(data);
            return;
        }

        Account receiver = Database.getInstance().getAccount(commandInput.getReceiver());
        double amount = commandInput.getAmount() * CurrencyExchanger.getRate(new Currencies<>(sender.getCurrency(), receiver.getCurrency()));
        sender.setBalance(sender.getBalance() - commandInput.getAmount());
        receiver.setBalance(receiver.getBalance() + amount);

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", commandInput.getDescription());
        output.put("senderIBAN", commandInput.getAccount());
        output.put("receiverIBAN", commandInput.getReceiver());
        output.put("amount", commandInput.getAmount() + " " + sender.getCurrency());

        output.put("transferType", "sent");
        TransactionData senderData = new TransactionData(output.deepCopy(), sender.getIban());

        output.put("transferType", "received");
        TransactionData receiverData = new TransactionData(output.deepCopy(), receiver.getIban());

        Database.getInstance().getUser(commandInput.getEmail()).addTransaction(senderData);
        Database.getInstance().getUser(receiver.getOwner()).addTransaction(receiverData);
    }
}
