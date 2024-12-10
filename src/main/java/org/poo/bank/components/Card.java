package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

/**
 * This class stores and handles a Card's information.
 */
@Data
public final class Card {
    private boolean isOneTime;
    private String iban;
    private String cardNumber;
    private CardStatus status;

    /**
     * This enum is used to determine the card status.
     * It also stores the string equivalent for ease of use.
     */
    @Getter
    public enum CardStatus {
        ACTIVE("active"),
        FROZEN("frozen");

        private final String status;

        CardStatus(final String status) {
            this.status = status;
        }
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param iban The IBAN of the Account the Card is attached to.
     */
    public Card(final String iban) {
        this.isOneTime = false;
        this.iban = iban;
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param iban The IBAN of the Account the Card is attached to.
     * @param isOneTime A boolean value specifying if a Card is one time use or not.
     */
    public Card(final String iban, final boolean isOneTime) {
        this.isOneTime = isOneTime;
        this.iban = iban;
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param commandInput The commandInput containing Card information.
     */
    public Card(final CommandInput commandInput) {
        this.isOneTime = false;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * This constructor creates a Card for a provided account.
     * @param commandInput The commandInput containing Card information.
     * @param isOneTime A boolean value specifying if a Card is one time use or not.
     */
    public Card(final CommandInput commandInput, final boolean isOneTime) {
        this.isOneTime = isOneTime;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    /**
     * This method converts a Card's information to JSON format.
     * @return An ObjectNode containing a Card's information.
     */
    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("cardNumber", cardNumber);
        output.put("status", status.getStatus());
        return output;
    }

    /**
     * This method provides the output for when a card is destroyed.
     * @param timestamp The timestamp of the destruction.
     * @return An ObjectNode containing the output data.
     */
    public ObjectNode destructionOutput(final int timestamp) {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByCard(cardNumber);

        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "The card has been destroyed");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return output;
    }

    /**
     * This method provides the output for when a card is created.
     * @param timestamp The timestamp of the creation.
     * @return An ObjectNode containing the output data.
     */
    public ObjectNode creationOutput(final int timestamp) {
        DatabaseEntry entry = Bank.getInstance().getDatabase().getEntryByCard(cardNumber);

        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return output;
    }
}
