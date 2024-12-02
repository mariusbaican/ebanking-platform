package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.bank.components.User;

@Data
public class Bank {
    private static Bank bank = new Bank();
    private User activeUser;
    private ObjectMapper objectMapper;

    private Bank() {
        activeUser = null;
        objectMapper = new ObjectMapper();
    }

    public static Bank getInstance() {
        return bank;
    }
}
