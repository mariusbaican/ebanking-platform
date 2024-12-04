package org.poo.bank.components;

import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.accounts.ClassicAccount;
import org.poo.bank.components.accounts.SavingsAccount;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class AccountBuilder {
    public static Account build(CommandInput commandInput) {
        String iban = Utils.generateIBAN();
        switch (commandInput.getAccountType()) {
            case "savings" -> {
                return new SavingsAccount(commandInput, iban);
            }
            case "classic" -> {
                return new ClassicAccount(commandInput, iban);
            }
            default ->
                throw new IllegalArgumentException("Unknown account type: " + commandInput.getAccountType());
        }
    }
}
