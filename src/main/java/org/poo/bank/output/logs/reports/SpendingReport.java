package org.poo.bank.output.logs.reports;

import org.poo.bank.Bank;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.visitor.OutputVisitor;

public class SpendingReport extends Report {
    public SpendingReport(String iban) {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByAccount(iban);
        account = entry.getAccount(iban);
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertSpendingReport(this);
    }
}
