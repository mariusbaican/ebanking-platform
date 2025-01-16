package org.poo.bank.payments;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.Tuple;
import org.poo.bank.components.ServicePlanHandler;
import org.poo.bank.components.accounts.Account;

@Data
public class WithdrawCash extends Transaction {
    private final Account account;
    private final double amount;
    private double adjustedAmount;

    public WithdrawCash(Account account, double amount) {
        this.account = account;
        this.amount = amount;
        this.adjustedAmount = ServicePlanHandler.adjustFeeForPlan(account.getServicePlan(), amount);
        this.adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>("RUN", account.getCurrency()), adjustedAmount);
    }

    @Override
    public void complete() {
        account.setBalance(account.getBalance() - amount);
    }
}
