package org.poo.bank.components.commerciants;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.fileio.CommerciantInput;

import java.util.Objects;

/**
 * This class is used to store a commerciant and the total amount spent on it.
 */
@Data
public final class Commerciant {
    private String name;
    private double total;
    private String iban;
    private String type;
    private String cashbackStrategy;

    /**
     * This constructor creates a Commerciant object.
     * @param name The commerciant's name.
     * @param total The total amount spent.
     */
    public Commerciant(final String name, final double total) {
        this.name = name;
        this.total = total;
    }

    public Commerciant (final CommerciantInput commerciantInput, final double total) {
        this.name = commerciantInput.getCommerciant();
        this.total =  total;
        this.iban = commerciantInput.getAccount();
        this.type = commerciantInput.getType();
        this.cashbackStrategy = commerciantInput.getCashbackStrategy();
    }

    /**
     * This method provides a Commerciant's information in JSON format.
     * @return An ObjectNode containing a Caommerciant's information.
     */
    public ObjectNode toJson() {
        ObjectNode commerciant = Bank.getInstance().createObjectNode();
        commerciant.put("commerciant", name);
        commerciant.put("total", total);
        return commerciant;
    }

    /**
     * This method is used to compare only the Commerciant's name.
     * @param obj The object to be compared.
     * @return True if they have the same name. False otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Commerciant c) {
            return c.name.equals(this.name);
        }
        return false;
    }

    /**
     * This method is used to generate a Commerciant's hashCode based off its name.
     * @return The Commerciant's hashCde
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
