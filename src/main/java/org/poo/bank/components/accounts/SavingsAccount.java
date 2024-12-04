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

    public boolean addInterest() {
        balance += interestRate * balance;
        return true;
    }

    public boolean setInterest(double interestRate) {
        this.interestRate = interestRate;
        return true;
    }
}
