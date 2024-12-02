package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

@Data
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount() { }

    public SavingsAccount(CommandInput commandInput, String iban) {
        super(commandInput, iban);
        interestRate = commandInput.getInterestRate();
    }
}
