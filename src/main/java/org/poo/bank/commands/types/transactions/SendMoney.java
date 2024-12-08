package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class SendMoney extends Command {
    public SendMoney(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry senderEntry =
                Database.getInstance().getEntryByUser(commandInput.getEmail());
        if (senderEntry == null) {
            return;
        }

        Account senderAccount = senderEntry.getAccount(commandInput.getAccount());
        if (senderAccount == null) {
            return;
        }

        DatabaseEntry receiverEntry =
                Database.getInstance().getEntryByAccount(commandInput.getReceiver());
        if (receiverEntry == null) {
            return;
        }
        if (!senderEntry.getUser().getEmail().equals(commandInput.getEmail())) {
            return;
        }

        Account receiverAccount =
                receiverEntry.getAccount(commandInput.getReceiver());

        if (Double.compare(senderAccount.getBalance(), commandInput.getAmount()) < 0) {
            output.put("timestamp", commandInput.getTimestamp());
            output.put("description", "Insufficient funds");

            TransactionData data =
                    new TransactionData(output.deepCopy(), senderAccount.getIban());
            senderEntry.getUser().addTransaction(data);

            return;
        }

        double paid =
                senderAccount.pay(commandInput.getAmount(), senderAccount.getCurrency());
        double received =
                receiverAccount.receive(commandInput.getAmount(), senderAccount.getCurrency());

        output.put("timestamp", commandInput.getTimestamp());
        output.put("description", commandInput.getDescription());
        output.put("senderIBAN", commandInput.getAccount());
        output.put("receiverIBAN", commandInput.getReceiver());

        output.put("amount", paid + " " + senderAccount.getCurrency());
        output.put("transferType", "sent");
        TransactionData senderData =
                new TransactionData(output.deepCopy(), senderAccount.getIban());

        output.put("amount", received + " " + receiverAccount.getCurrency());
        output.put("transferType", "received");
        TransactionData receiverData =
                new TransactionData(output.deepCopy(), receiverAccount.getIban());

        senderEntry.getUser().addTransaction(senderData);
        receiverEntry.getUser().addTransaction(receiverData);
    }
}
