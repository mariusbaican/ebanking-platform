package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

@Data
public final class ClassicAccount extends Account {
    public ClassicAccount() { }

    public ClassicAccount(final CommandInput commandInput, final String iban) {
        super(commandInput, iban);
    }

    public boolean addInterest() {
        return false;
    }

    public boolean setInterest(final double interestRate) {
        return false;
    }

    public boolean isSavingsAccount() {
        return false;
    }
}
