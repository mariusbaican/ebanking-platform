package org.poo.bank.components.commerciants;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class is used to handle a group of Commerciant objects.
 */
public final class CommerciantGroup {
    private final List<Commerciant> commerciantList;

    /**
     * This constructor initializes the Commerciant List.
     */
    public CommerciantGroup() {
        this.commerciantList = new ArrayList<>();
    }

    /**
     * This method is used to add a Commerciant to the List. If the
     * same Commerciant name is already present in the List, their amounts
     * are added up.
     * @param commerciant The Commerciant to be added to the List.
     */
    public void addCommerciant(final Commerciant commerciant) {
        if (!commerciantList.contains(commerciant)) {
            commerciantList.add(commerciant);
            return;
        }
        Commerciant existingCommerciant =
                commerciantList.get(commerciantList.indexOf(commerciant));
        existingCommerciant.setTotal(existingCommerciant.getTotal() + commerciant.getTotal());
    }

    /**
     * This method provides the List of Commerciants in JSON format.
     * They are sorted alphabetically by their name.
     * @return An ArrayNode containing the information of all Commerciant objects.
     */
    public ArrayNode toJson() {
        ArrayNode commerciants = Bank.getInstance().createArrayNode();
        commerciantList.sort(Comparator.comparing(Commerciant::getName));
        for (Commerciant commerciant : commerciantList) {
            commerciants.add(commerciant.toJson());
        }
        return commerciants;
    }
}
