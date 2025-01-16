package org.poo.bank.components.accounts;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BusinessAccount extends Account {
    private String manager;
    private String employee;

    @Override
    public boolean addInterest() {
        return false;
    }

    @Override
    public boolean setInterest(double interestRate) {
        return false;
    }

    @Override
    public ObjectNode addInterestJson(int timestamp) {
        return null;
    }

    @Override
    public ObjectNode changeInterestRateJson(int timestamp, double newInterestRate) {
        return null;
    }
}
