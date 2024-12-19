package org.poo.bank.components;

import lombok.Data;
import org.poo.bank.commands.output.visitor.OutputVisitor;
import org.poo.bank.commands.output.visitor.Visitable;
import org.poo.fileio.UserInput;

/**
 * This class is used to store and handle a User's information.
 */
@Data
public final class User implements Visitable {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String occupation;

    /**
     * This constructor creates a User object based on a provided input.
     * @param userInput The desired User information.
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        //TODO ADD AFTER UPDATING CHECKER
        /*
        this.birthDate = userInput.getBirthDate();
        this.occupation = userInput.getOccupation();
         */
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertUser(this);
    }
}
