package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

/**
 * This class stores a SavingsAccount's data.
 */
@Data
public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount() { }

    /**
     * This constructor creates a SavingsAccount based on a command's input.
     * @param commandInput The desired account information.
     */
    public SavingsAccount(final CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

    /**
     * This method is used to add the interest to a SavingsAccount.
     * @return Always true as this is a SavingsAccount.
     */
    public boolean addInterest() {
        balance += interestRate * balance;
        return true;
    }

    /**
     * This method is used to set the interest of a SavingsAccount.
     * @return Always true as this is a SavingsAccount.
     */
    public boolean setInterest(final double newInterestRate) {
        this.interestRate = newInterestRate;
        return true;
    }

    /**
     * This method specifies if an account is a SavingsAccount.
     * @return Always true as this is a SavingsAccount.
     */
    public boolean isSavingsAccount() {
        return true;
    }
}
