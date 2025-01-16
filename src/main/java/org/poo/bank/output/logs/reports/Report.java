package org.poo.bank.output.logs.reports;

import lombok.Data;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.output.logs.TransactionHistory;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;

@Data
public abstract class Report implements Visitable {
    protected Account account;
    TransactionHistory transactionHistory;

    public enum ReportType {
        ACCOUNT,
        SPENDING,
        BUSINESS
    }

    public Report setMinTimestamp(int timestamp) {
        transactionHistory.setMinTimestamp(timestamp);
        return this;
    }
    public Report setMaxTimestamp(int timestamp) {
        transactionHistory.setMaxTimestamp(timestamp);
        return this;
    }
    public abstract <T> T accept(OutputVisitor<T> outputVisitor);
}
