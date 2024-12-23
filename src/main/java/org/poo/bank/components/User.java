package org.poo.bank.components;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.output.visitor.OutputVisitor;
import org.poo.bank.output.visitor.Visitable;
import org.poo.fileio.UserInput;

import java.util.Calendar;

/**
 * This class is used to store and handle a User's information.
 */
@Data
public final class User implements Visitable {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private int age;
    private String occupation;

    /**
     * This constructor creates a User object based on a provided input.
     * @param userInput The desired User information.
     */
    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.birthDate = userInput.getBirthDate();
        this.age = computeAge(birthDate);
        this.occupation = userInput.getOccupation();
    }

    private int computeAge(String birthDate) {
        String[] parts = birthDate.split("-");
        int userYear = Integer.parseInt(parts[0]);
        int userMonth = Integer.parseInt(parts[1]);
        int userDay = Integer.parseInt(parts[2]);
        int currYear = Bank.getInstance().getCalendar().get(Calendar.YEAR);
        int currMonth = Bank.getInstance().getCalendar().get(Calendar.MONTH);
        int currDay = Bank.getInstance().getCalendar().get(Calendar.DAY_OF_MONTH);
        int userAge = currYear - userYear;

        if (currMonth < userMonth || (currMonth == userMonth && currDay < userDay)) {
            userAge--;
        }
        return userAge;
    }

    @Override
    public <T> T accept(OutputVisitor<T> outputVisitor) {
        return outputVisitor.convertUser(this);
    }
}
