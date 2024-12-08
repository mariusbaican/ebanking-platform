package org.poo.bank.components.commerciants;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class Expenses {
    private static final List<CommerciantCategory> CATEGORIES = new ArrayList<>();

    private Expenses() { }

    public static void reset() {
        CATEGORIES.clear();
    }

    public static CommerciantCategory getCategory(final int id) {
        return CATEGORIES.get(id);
    }

    public static void addCategory(final CommerciantCategory category) {
        CATEGORIES.add(category.getId(), category);
    }
}
