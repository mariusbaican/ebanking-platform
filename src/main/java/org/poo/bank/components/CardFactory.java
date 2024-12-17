package org.poo.bank.components;

import org.poo.bank.components.cards.Card;
import org.poo.bank.components.cards.OneTimeCard;
import org.poo.fileio.CommandInput;

/**
 * This static class is used to generate every card type.
 * It implements the Factory design pattern.
 */
public final class CardFactory {
    /**
     * This method generates a Card based on a given input.
     * @param commandInput The general input of a command.
     * @param isOneTime Whether the desired card is a OneTimeCard or not
     * @return An instance of the requested cardType.
     */
    public static Card generate(final CommandInput commandInput, final boolean isOneTime) {
        if (isOneTime) {
            return new OneTimeCard(commandInput);
        }
        return new Card(commandInput);
    }

    /**
     * This method generates a Card based on a given input.
     * @param iban The IBAN of the owner account.
     * @param isOneTime Whether the desired card is a OneTimeCard or not
     * @return An instance of the requested cardType.
     */
    public static Card generate(final String iban, final boolean isOneTime) {
        if (isOneTime) {
            return new OneTimeCard(iban);
        } else {
            return new Card(iban);
        }
    }

    private CardFactory() { }
}
