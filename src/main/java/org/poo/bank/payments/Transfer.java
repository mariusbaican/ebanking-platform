package org.poo.bank.payments;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.Tuple;
import org.poo.bank.components.ServicePlanHandler;
import org.poo.bank.components.accounts.Account;

@Data
public class Transfer extends Transaction {
    private final Account senderAccount;
    private final Account receiverAccount;
    private final double amount;
    private final double sentAmount;
    private final double receivedAmount;

    public Transfer(Account senderAccount, Account receiverAccount, double amount) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.sentAmount = ServicePlanHandler.adjustFeeForPlan(senderAccount.getServicePlan(), amount);
        this.receivedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(senderAccount.getCurrency(), receiverAccount.getCurrency()), amount);
        this.transactionType = TransactionType.TRANSFER;
    }

    @Override
    public void complete() {
        senderAccount.setBalance(senderAccount.getBalance() - sentAmount);
        receiverAccount.setBalance(receiverAccount.getBalance() + receivedAmount);
    }
}
