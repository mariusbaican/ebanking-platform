package org.poo.bank.components;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

@Data
public class Card {
    private boolean isOneTime;
    private String iban;
    private String cardNumber;

    public Card(CommandInput commandInput) {
        this.isOneTime = false;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
    }

    public Card(CommandInput commandInput, boolean isOneTime) {
        this.isOneTime = isOneTime;
        iban = commandInput.getAccount();
        this.cardNumber = Utils.generateCardNumber();
    }

    public ObjectNode toJson() {
        ObjectNode output = Bank.getInstance().getObjectMapper().createObjectNode();
        output.put("cardNumber", cardNumber);
        Account account = Database.getInstance().getAccount(iban);
        if (account.getMinBalance() == -1) {
            output.put("status", "active");
        } else {
            if (Double.compare(account.getBalance(), account.getMinBalance()) > 0) {
                if (account.getBalance() - account.getMinBalance() > 30) {
                    output.put("status", "active");
                } else {
                    output.put("status", "warning");
                }
            } else {
                output.put("status", "frozen");
            }
        }
        return output;
    }
}
