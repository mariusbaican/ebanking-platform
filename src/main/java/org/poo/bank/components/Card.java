package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

@Data
public class Card {
    private boolean isOneTime;
    private String iban;
    private String cardNumber;
    private CardStatus status;

    @Getter
    public enum CardStatus {
        ACTIVE("active"),
        FROZEN ("frozen");

        private final String status;

        CardStatus(String status) {
            this.status = status;
        }
    }

    public Card(CommandInput commandInput) {
        this.isOneTime = false;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public Card(CommandInput commandInput, boolean isOneTime) {
        this.isOneTime = isOneTime;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
        status = CardStatus.ACTIVE;
    }

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().getObjectMapper().createObjectNode();
        output.put("cardNumber", cardNumber);
        output.put("status", status.getStatus());
        return output;
    }
}
