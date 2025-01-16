package org.poo.bank.output.logs;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.User;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionHistory implements Visitable {
    List<TransactionData> transactions;

    public TransactionHistory(Account account) {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByAccount(account.getIban());
        transactions = entry.getTransactionHistory().stream().filter(t -> t.account().equals(account.getIban())).toList();
    }

    public TransactionHistory(User user) {
        transactions = new ArrayList<>(Bank.getInstance().getDatabase().getEntryByUser(user.getEmail()).getTransactionHistory());
    }

    public TransactionHistory setMinTimestamp(int timestamp) {
        transactions = transactions.stream().filter(t -> t.data().get("timestamp").asInt() >= timestamp).toList();
        return this;
    }

    public TransactionHistory setMaxTimestamp(int timestamp) {
        transactions = transactions.stream().filter(t -> t.data().get("timestamp").asInt() <= timestamp).toList();
        return this;
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertTransactionHistory(this);
    }
}
