package org.poo.bank.components.cards;

import org.poo.bank.Bank;
import org.poo.bank.components.CardFactory;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.output.logs.Response;
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

            Card card = CardFactory.generate(iban, CardType.ONE_TIME);
            Bank.getInstance().getDatabase().addCard(entry.getUser().getEmail(), card);
            entry.addTransaction(card.creationTransaction(entry, timestamp));
        }
    }

    @Override
    public void replace() {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByCard(cardNumber);

        Bank.getInstance().getDatabase().removeCard(cardNumber);
        entry.addTransaction(new Response()
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("description", "The card has been destroyed")
                .addField("card", cardNumber)
                .addField("cardHolder", entry.getUser().getEmail())
                .addField("account", iban)
                .asTransactionData(iban)
        );

        Card card = CardFactory.generate(iban, CardType.ONE_TIME);
        Bank.getInstance().getDatabase().addCard(entry.getUser().getEmail(), card);
        entry.addTransaction(new Response()
                .addField("timestamp", Bank.getInstance().getTimestamp())
                .addField("description", "New card created")
                .addField("card", card.getCardNumber())
                .addField("cardHolder", entry.getUser().getEmail())
                .addField("account", iban)
                .asTransactionData(iban)
        );

    }

}
