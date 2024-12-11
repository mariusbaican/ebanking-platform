package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This subclass of Command has the purpose of executing a splitPayment request
 */
public final class SplitPayment extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public SplitPayment(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a splitPayment request.
     * It checks that every provided account is existent and that
     * they all can afford the transaction. In case of a non-existent account
     * the method exits. If one or more accounts cannot afford the transaction,
     * all users get an error in their transaction history. Otherwise, they receive
     * a confirmation message.
     */
    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();

        for (String iban : commandInput.getAccounts()) {
            DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByAccount(iban);
            if (entry == null) {
                return;
            }
            accounts.add(entry.getAccount(iban));
        }

        double price = commandInput.getAmount() / accounts.size();

        output.put("timestamp", commandInput.getTimestamp());
        DecimalFormat df = new DecimalFormat("0.00");
        output.put("description", "Split payment of "
                + df.format(commandInput.getAmount()) + " "
                + commandInput.getCurrency());
        output.put("currency", commandInput.getCurrency());
        output.put("amount", price);

        ArrayNode ibanArray = Bank.getInstance().createArrayNode();
        for (Account account : accounts) {
            ibanArray.add(account.getIban());
        }
        output.put("involvedAccounts", ibanArray);

        Account poorAccount = null;
        for (Account account : accounts) {
            double ret = account.canAfford(price, commandInput.getCurrency());
            if (ret == 0.0) {
                poorAccount = account;
            }
        }

        if (poorAccount != null) {
            output.put("error", "Account " + poorAccount.getIban()
                    + " has insufficient funds for a split payment.");
        }

        for (Account account : accounts) {
            if (poorAccount == null) {
                account.pay(price, commandInput.getCurrency());
            }
            Bank.getInstance().getDatabase().getEntryByUser(account.getOwner())
                    .addTransaction(new TransactionData(output.deepCopy(), account.getIban()));
        }
    }
}
