package org.poo.bank.components.commerciants;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Expenses {
    private static List<CommerciantCategory> categories = new ArrayList<CommerciantCategory>();

    public static void reset() {
        categories.clear();
    }

    public static CommerciantCategory getCategory(int id) {
        return categories.get(id);
    }

    public static void addCategory(CommerciantCategory category) {
        categories.add(category.getId(), category);
    }
}
