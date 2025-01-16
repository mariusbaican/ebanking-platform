package org.poo.bank.payments;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.Tuple;
import org.poo.bank.components.ServicePlanHandler;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.cards.Card;

@Data
public class CardPayment extends Transaction {
    private final Card card;
    private final Account account;
    private final double amount;
    private double adjustedAmount;
    private final String currency;

    public CardPayment(Card card, double amount, String currency) {
        this.card = card;
        this.account = Bank.getInstance().getDatabase().getEntryByCard(card.getCardNumber()).getAccount(card.getIban());
        this.amount = amount;
        this.adjustedAmount = ServicePlanHandler.adjustFeeForPlan(account.getServicePlan(), amount);
        this.currency = currency;
        this.adjustedAmount = Bank.getInstance().getCurrencyExchanger().exchange(new Tuple<>(currency, account.getCurrency()), adjustedAmount);
        this.transactionType = TransactionType.CARD_PAYMENT;
    }

    @Override
    public void complete() {
        account.setBalance(account.getBalance() - adjustedAmount);
        card.replace();
    }
}
