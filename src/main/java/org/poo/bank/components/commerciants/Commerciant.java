package org.poo.bank.components.commerciants;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;

import java.util.Objects;

/**
 * This class is used to store a commerciant and the total amount spent on it.
 */
@Data
public final class Commerciant {
    private final String name;
    private double total;

    /**
     * This constructor creates a Commerciant object.
     * @param name The commerciant's name.
     * @param total The total amount spent.
     */
    public Commerciant(final String name, final double total) {
        this.name = name;
        this.total = total;
    }

    /**
     * This method provides a Commerciant's information in JSON format.
     * @return An ObjectNode containing a Commerciant's information.
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
