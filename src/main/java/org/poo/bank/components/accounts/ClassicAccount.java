package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

/**
 * This class stores a ClassicAccount's data.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ClassicAccount extends Account {
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

    /**
     * This method provides a JSON format message for an addInterest request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the error message.
     */
    public ObjectNode addInterestJson(final int timestamp) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "This is not a savings account");

        return output;
    }

    /**
     * This method provides a JSON format message for a changeInterest request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the error message.
     */
    public ObjectNode changeInterestRateJson(final int timestamp, final double newInterestRate) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "This is not a savings account");

        return output;
    }
}
