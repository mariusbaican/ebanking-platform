package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

/**
 * This class stores a SavingsAccount's data.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class SavingsAccount extends Account {
    private double interestRate;
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

    /**
     * This method provides a JSON format message for an addInterest request.
     * @param timestamp The timestamp of the request.
     * @return A null ObjectNode as no message is required for this request.
     */
    public ObjectNode addInterestJson(final int timestamp) {
        return null;
    }

    /**
     * This method provides a JSON format message for an addInterest request.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the information.
     */
    public ObjectNode changeInterestRateJson(final int timestamp, final double newInterestRate) {
        ObjectNode output = Bank.getInstance().createObjectNode();
        output.put("description",
                "Interest rate of the account changed to " + newInterestRate);
        output.put("timestamp", timestamp);

        return output;
    }
}
