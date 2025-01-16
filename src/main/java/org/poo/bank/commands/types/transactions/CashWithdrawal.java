package org.poo.bank.commands.types.transactions;

import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.payments.WithdrawCash;
import org.poo.fileio.CommandInput;

public class CashWithdrawal extends Command {
    public CashWithdrawal(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByCard(commandInput.getEmail());
        if (entry == null) {
            //User not found
            return;
        }

        Card card = entry.getCard(commandInput.getCardNumber());
        if (card == null) {
            //Card not found
            return;
        }

        Account account = entry.getAccount(card.getIban());

        WithdrawCash withdrawCash = new WithdrawCash(account, commandInput.getAmount());
        if (Double.compare(account.getBalance(), withdrawCash.getAdjustedAmount()) < 0) {
            // Insufficient funds
            return;
        }
        Bank.getInstance().getPaymentHandler().addPayment(withdrawCash);
        //Cash withdrawal of ${amount}
    }


}
