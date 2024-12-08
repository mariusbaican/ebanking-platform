package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

@Data
public final class Card {
    private boolean isOneTime;
    private String iban;
    private String cardNumber;
    private CardStatus status;

    @Getter
    public enum CardStatus {
        ACTIVE("active"),
        FROZEN("frozen");

        private final String status;

        CardStatus(final String status) {
            this.status = status;
        }
    }

    public Card(final String iban) {
        this.isOneTime = false;
        this.iban = iban;
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public Card(final String iban, final boolean isOneTime) {
        this.isOneTime = isOneTime;
        this.iban = iban;
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public Card(final CommandInput commandInput) {
        this.isOneTime = false;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public Card(final CommandInput commandInput, final boolean isOneTime) {
        this.isOneTime = isOneTime;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("cardNumber", cardNumber);
        output.put("status", status.getStatus());
        return output;
    }

    public ObjectNode destroyOutput(final int timestamp) {
        DatabaseEntry entry = Database.getInstance().getEntryByCard(cardNumber);

        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "The card has been destroyed");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return output;
    }

    public ObjectNode createOutput(final int timestamp) {
        DatabaseEntry entry = Database.getInstance().getEntryByCard(cardNumber);

        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "New card created");
        output.put("card", cardNumber);
        output.put("cardHolder", entry.getUser().getEmail());
        output.put("account", iban);

        return output;
    }
}
