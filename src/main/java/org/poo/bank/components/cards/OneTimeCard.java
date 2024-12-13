package org.poo.bank.components.cards;

import org.poo.bank.Bank;
import org.poo.bank.components.CardFactory;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class OneTimeCard extends Card {

    public OneTimeCard(final String iban) {
        super(iban);
    }

    public OneTimeCard(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * The pay method handles all behaviors for frozen cards, insufficient funds,
     * and replacing oneTimeCards. It also adds the corresponding transactions
     * to the databaseEntry for every scenario.
     * @param entry The databaseEntry containing the Card.
     * @param amount The amount to be paid.
     * @param requestedCurrency The currency of the requestedAmount.
     * @param timestamp The timestamp of the payment.
     * @param commerciant The commerciant of the payment.
     */
    public void pay(final DatabaseEntry entry, final double amount,
                    final String requestedCurrency, final int timestamp,
                    final String commerciant) {
        if (status == CardStatus.FROZEN) {
            entry.addTransaction(frozenCardTransaction(timestamp));
            return;
        }

        double sumPaid = entry.getAccount(iban).pay(amount, requestedCurrency);

        if (Double.compare(sumPaid, 0.0) == 0) {
            entry.addTransaction(insufficientFundsTransaction(timestamp));
        } else {
            entry.addTransaction(paymentTransaction(timestamp, sumPaid, commerciant));

            entry.addTransaction(destructionTransaction(entry, timestamp));
            Bank.getInstance().getDatabase().removeCard(cardNumber);

            Card card = CardFactory.build(iban, true);
            entry.addCard(card);
            entry.addTransaction(card.creationTransaction(entry, timestamp));
        }
    }

}
