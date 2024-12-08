package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.currency.Currencies;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SplitPayment extends Command {
    public SplitPayment(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();

        for (String iban : commandInput.getAccounts()) {
            Account account = Database.getInstance().getAccount(iban);
            if (account == null) {
                return;
            }
            accounts.add(account);
        }

        double price = commandInput.getAmount() / accounts.size();

        output.put("timestamp", commandInput.getTimestamp());
        DecimalFormat df = new DecimalFormat("0.00");
        output.put("description", "Split payment of " + df.format(commandInput.getAmount()) + " " + commandInput.getCurrency());
        output.put("currency", commandInput.getCurrency());
        output.put("amount", price);
        ArrayNode ibanArray = Bank.getInstance().getObjectMapper().createArrayNode();
        for (Account account : accounts) {
            ibanArray.add(account.getIban());
            account.setBalance(account.getBalance() - price * CurrencyExchanger.getRate(new Currencies<>(commandInput.getCurrency(), account.getCurrency())));
        }
        output.put("involvedAccounts", ibanArray);
        for (Account account : accounts) {
            TransactionData data = new TransactionData(output.deepCopy(), account.getIban());
            Database.getInstance().getUser(account.getOwner()).addTransaction(data);
        }
    }
}
