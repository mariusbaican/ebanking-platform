package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.output.logs.Response;
import org.poo.bank.output.logs.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

/**
 * This subclass of Command has the purpose of executing a changeInterestRate request
 */
public final class ChangeInterestRate extends Command {
    /**
     * This constructor calls the Command superclass constructor.
     * It stores the commandInput for further use during execution.
     * @param commandInput The input for the requested command.
     */
    public ChangeInterestRate(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to execute a changeInterestRate request.
     * It handles the error of a non-existent account being provided
     * or a classicAccount being provided.
     * If the account is non-existent, the method exits. If the account is
     * not a savingsAccount the result is added to the global JSON
     * output. If the account is valid, a confirmation transaction is added
     * to the user's transaction history.
     */
    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase()
                .getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account account = entry.getAccount(commandInput.getAccount());
        if (account.getAccountType() != Account.AccountType.SAVINGS) {
            Bank.getInstance().addToOutput(new Response()
                    .addField("command", commandInput.getCommand())
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("output", new Response()
                            .addField("timestamp", Bank.getInstance().getTimestamp())
                            .addField("description", "This is not a savings account")
                            .asObjectNode()
                    )
                    .asObjectNode()
            );
        } else {
            entry.addTransaction(new Response()
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("description", "Interest rate of the account changed to " + commandInput.getInterestRate())
                    .asTransactionData(account.getIban())
            );
        }
    }
}
