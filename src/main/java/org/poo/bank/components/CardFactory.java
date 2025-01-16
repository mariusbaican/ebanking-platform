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
     * @param cardType The desired cardType
     * @return An instance of the requested cardType.
     */
    public static Card generate(final CommandInput commandInput, Card.CardType cardType) {
        switch (cardType) {
            case REGULAR:
                return new Card(commandInput);
            case ONE_TIME:
                return new OneTimeCard(commandInput);
            default:
                throw new IllegalArgumentException("Invalid card type: " + cardType);
        }
    }

    /**
     * This method generates a Card based on a given input.
     * @param iban The IBAN of the owner account.
     * @param cardType The desired cardType
     * @return An instance of the requested cardType.
     */
    public static Card generate(final String iban, Card.CardType cardType) {
        switch (cardType) {
            case REGULAR:
                return new Card(iban);
            case ONE_TIME:
                return new OneTimeCard(iban);
            default:
                throw new IllegalArgumentException("Invalid card type: " + cardType);
        }
    }

    private CardFactory() { }
}
