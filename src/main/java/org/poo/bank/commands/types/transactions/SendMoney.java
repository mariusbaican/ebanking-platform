package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.output.logs.Response;
import org.poo.bank.output.logs.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.payments.Transfer;
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
            senderEntry.addTransaction(new Response()
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("description", "Insufficient funds")
                    .asTransactionData(senderAccount.getIban())
            );
            return;
        }

        Transfer transfer = new Transfer(senderAccount, receiverAccount, commandInput.getAmount());
        Bank.getInstance().getPaymentHandler().addPayment(transfer);

        senderEntry.addTransaction(new Response()
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("description", commandInput.getDescription())
                .addField("senderIBAN", commandInput.getAccount())
                .addField("receiverIBAN", commandInput.getReceiver())
                .addField("amount", transfer.getSentAmount() + " " + senderAccount.getCurrency())
                .addField("transferType", "sent")
                .asTransactionData(senderAccount.getIban())
        );

        receiverEntry.addTransaction(new Response()
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("description", commandInput.getDescription())
                .addField("senderIBAN", commandInput.getAccount())
                .addField("receiverIBAN", commandInput.getReceiver())
                .addField("amount", transfer.getReceivedAmount() + " " + receiverAccount.getCurrency())
                .addField("transferType", "received")
                .asTransactionData(receiverAccount.getIban())
        );
    }
}
