package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

@Data
public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount() { }

    public SavingsAccount(final CommandInput commandInput, final String iban) {
        super(commandInput, iban);
        interestRate = commandInput.getInterestRate();
    }

    public boolean addInterest() {
        balance += interestRate * balance;
        return true;
    }

    public boolean setInterest(final double interestRate) {
        this.interestRate = interestRate;
        return true;
    }

    public boolean isSavingsAccount() {
        return true;
    }
}
