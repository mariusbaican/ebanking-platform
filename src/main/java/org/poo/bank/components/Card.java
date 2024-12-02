package org.poo.bank.components;

import lombok.Data;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

@Data
public class Card {
    private Account account;
    private boolean isOneTime;
    private String cardNumber;
    private String iban;

    public Card(CommandInput commandInput, String cardNumber) {
        account = Database.getInstance().getAccount(commandInput.getAccount());
        this.isOneTime = false;
        this.cardNumber = cardNumber;
        iban = commandInput.getAccount();
    }

    public Card(CommandInput commandInput, String cardNumber, boolean isOneTime) {
        account = Database.getInstance().getAccount(commandInput.getAccount());
        this.isOneTime = isOneTime;
        this.cardNumber = cardNumber;
        iban = commandInput.getAccount();
    }
}
