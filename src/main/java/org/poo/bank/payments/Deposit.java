package org.poo.bank.payments;

import org.poo.bank.components.ServicePlanHandler;
import org.poo.bank.components.accounts.Account;

public class Deposit extends Transaction {
    private final Account account;
    private final double amount;

    public Deposit(Account account, double amount) {
        this.account = account;
        this.amount = amount;
        this.transactionType = TransactionType.DEPOSIT;
    }

    @Override
    public void complete() {
        account.addFunds(ServicePlanHandler.adjustFeeForPlan(account.getServicePlan(), amount));
    }
}
