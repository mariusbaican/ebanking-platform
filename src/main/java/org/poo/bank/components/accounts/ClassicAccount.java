package org.poo.bank.components.accounts;

import lombok.Data;
import org.poo.fileio.CommandInput;

@Data
public class ClassicAccount extends Account {
    public ClassicAccount() { }

    public ClassicAccount(CommandInput commandInput, String iban) {
        super(commandInput, iban);
    }

    public boolean addInterest() {
        return false;
    }

    public boolean setInterest(double interestRate) {
        return false;
    }
}
