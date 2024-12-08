package org.poo.bank.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

@Data
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private ObjectMapper objectMapper;
    private List<TransactionData> transactionHistory;

    public User(final UserInput userInput, final ObjectMapper objectMapper) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.objectMapper = objectMapper;
        this.transactionHistory = new ArrayList<>();
    }

    public void addTransaction(final TransactionData transactionData) {
        transactionHistory.add(transactionData);
    }
}
