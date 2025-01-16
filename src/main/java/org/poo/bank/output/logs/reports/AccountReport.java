package org.poo.bank.output.logs.reports;

import org.poo.bank.Bank;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.visitor.OutputVisitor;

public class AccountReport extends Report {
    public AccountReport(String iban) {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByAccount(iban);
        account = entry.getAccount(iban);
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertAccountReport(this);
    }
}
