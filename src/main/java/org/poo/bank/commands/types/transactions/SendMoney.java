package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a sendMoney request
 */
public final class SendMoney extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public SendMoney(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a sendMoney request.
     * In case of an invalid requesting user, invalid sending or receiving account
     * the method exits. If the sender cannot afford the transfer, an error
     * message gets added to his transaction history. If successful, a message
     * is added to both the sender and the receiver's history.
     */
    @Override
    public void run() {
        DatabaseEntry senderEntry =
                Bank.getInstance().getDatabase().getEntryByUser(commandInput.getEmail());
        if (senderEntry == null) {
            return;
        }

        Account senderAccount = senderEntry.getAccount(commandInput.getAccount());
        if (senderAccount == null) {
            return;
        }

        DatabaseEntry receiverEntry =
                Bank.getInstance().getDatabase().getEntryByAccount(commandInput.getReceiver());
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

            senderEntry.getUser().addTransaction(
                    new TransactionData(output.deepCopy(), senderAccount.getIban()));

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
