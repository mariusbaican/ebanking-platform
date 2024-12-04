package org.poo.bank.components;

import lombok.Data;
import org.poo.fileio.CommerciantInput;

import java.util.ArrayList;

@Data
public class Category {
    private int id;
    private String description;
    private ArrayList<String> commerciants;

    public Category(CommerciantInput commerciantInput) {
        this.id = commerciantInput.getId();
        this.description = commerciantInput.getDescription();
    }
}
