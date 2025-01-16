package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
import org.poo.fileio.CommandInput;

public class WithdrawSavings extends Command {
    public WithdrawSavings(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            return;
        }

        Account savingsAccount = entry.getAccount(commandInput.getAccount());
        if (savingsAccount.getAccountType() != Account.AccountType.SAVINGS) {
            //Account is not of type savings
            return;
        }

        User user = entry.getUser();
        if (user.getAge() < 21) {
            entry.addTransaction(new Response()
                    .addField("timestamp", Bank.getInstance().getTimestamp())
                    .addField("description", "You don't have the minimum age required.")
                    .asTransactionData(savingsAccount.getIban())
            );
            return;
        }

        Account classicAccount = null; //= entry.getValidClassicAccount(commandInput.getCurrency());
        if (classicAccount == null) {
            //You do not have a classic account
            return;
        }

        double withdrawn = savingsAccount.withdrawFunds(commandInput.getAmount(), commandInput.getCurrency());
        if (Double.compare(withdrawn, 0.0) == 0) {
            //Insufficient funds
            return;
        }
        classicAccount.addFunds(commandInput.getAmount());
        /*entry.addTransaction(TransactionMessages.withdrawSavings(commandInput.getAmount(),
                classicAccount.getIban(), savingsAccount.getIban(), commandInput.getDescription(),
                commandInput.getTimestamp(), classicAccount.getIban()));
        entry.addTransaction(TransactionMessages.withdrawSavings(commandInput.getAmount(),
                classicAccount.getIban(), savingsAccount.getIban(), commandInput.getDescription(),
                commandInput.getTimestamp(), savingsAccount.getIban()));*/
    }
}
