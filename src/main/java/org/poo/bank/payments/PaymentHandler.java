package org.poo.bank.payments;

import java.util.ArrayList;
import java.util.List;

public class PaymentHandler {
    private static final PaymentHandler INSTANCE = new PaymentHandler();
    private final List<Transaction> activePayments;

    private PaymentHandler() {
        activePayments = new ArrayList<>();
    }

    public static PaymentHandler getInstance() {
        return INSTANCE;
    }

    public void reset() {
        activePayments.clear();
    }

    public void addPayment(Transaction payment) {
        if (payment.getTransactionType() == Transaction.TransactionType.CUSTOM_SPLIT_PAYMENT) {
            activePayments.add(payment);
            return;
        }
        payment.complete();
    }

    public void update() {

    }
}
