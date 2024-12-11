package org.poo.bank.components;

import lombok.Data;
import org.poo.fileio.UserInput;

/**
 * This class is used to store and handle a User's information.
 */
@Data
public final class User {
    private String firstName;
    private String lastName;
    private String email;

    /**
     * This constructor creates a User object based on a provided input.
     * @param userInput The desired User information.
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
    }
}
