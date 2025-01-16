package org.poo.bank.payments;

import org.poo.bank.components.accounts.Account;

public class ReceiveInterest extends Transaction {
    private final Account account;

    public ReceiveInterest(Account account) {
        this.account = account;
        this.transactionType = TransactionType.RECEIVE_INTEREST;
    }

    @Override
    public void complete() {
        account.addInterest();
    }
}
