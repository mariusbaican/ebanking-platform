package org.poo.bank.payments;

import lombok.Data;

@Data
public abstract class Transaction {
    public enum TransactionType {
        WITHDRAW_CASH,
        WITHDRAW_SAVINGS,
        CARD_PAYMENT,
        SPLIT_PAYMENT,
        CUSTOM_SPLIT_PAYMENT,
        TRANSFER,
        DEPOSIT,
        RECEIVE_INTEREST
    }

    protected TransactionType transactionType;

    public abstract void complete();
}
