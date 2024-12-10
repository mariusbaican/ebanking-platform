package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

/**
 * This class stores a ClassicAccount's data.
 */
@Data
public final class ClassicAccount extends Account {
    public ClassicAccount() { }

    /**
     * This constructor creates a ClassicAccount based on a command's input.
     * @param commandInput The desired account information.
     */
    public ClassicAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * This method is used to add the interest to a SavingsAccount.
     * @return Always false as this is not a SavingsAccount.
     */
    public boolean addInterest() {
        return false;
    }

    /**
     * This method is used to set the interest of a SavingsAccount.
     * @return Always false as this is not a SavingsAccount.
     */
    public boolean setInterest(final double interestRate) {
        return false;
    }

    /**
     * This method specifies if an account is a SavingsAccount.
     * @return Always false as this is not a SavingsAccount.
     */
    public boolean isSavingsAccount() {
        return false;
    }
}
