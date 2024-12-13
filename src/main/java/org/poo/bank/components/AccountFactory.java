package org.poo.bank.components;

import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.accounts.ClassicAccount;
import org.poo.bank.components.accounts.SavingsAccount;
import org.poo.fileio.CommandInput;

/**
 * This static class is used to generate every account type.
 * It implements the Factory design pattern.
 */
public final class AccountFactory {
    /**
     * This method generates an Account based on a given input.
     * @param commandInput The general input of a command.
     * @return An instance of the requested accountType.
     */
    public static Account build(final CommandInput commandInput) {
        switch (commandInput.getAccountType()) {
            case "savings" -> {
                return new SavingsAccount(commandInput);
            }
            case "classic" -> {
                return new ClassicAccount(commandInput);
            }
            default ->
                throw new IllegalArgumentException("Unknown account type: "
                        + commandInput.getAccountType());
        }
    }

    private AccountFactory() { }
}
