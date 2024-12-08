package org.poo.bank.components.commerciants;

import lombok.Data;
import org.poo.fileio.CommerciantInput;

import java.util.List;

@Data
public class CommerciantCategory {
    private int id;
    private String description;
    private List<String> commerciants;

    public CommerciantCategory(CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.description = commerciantInput.getDescription();
        this.commerciants = commerciantInput.getCommerciants();
    }

    public boolean hasCommerciant(String commerciant) {
        return commerciants.contains(commerciant);
    }
}
