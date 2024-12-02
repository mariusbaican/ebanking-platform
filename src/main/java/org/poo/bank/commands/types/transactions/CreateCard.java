package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.Card;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateCard extends Command {
    private boolean isOneTime;

    public CreateCard(CommandInput commandInput) {
        super(commandInput);
        isOneTime = false;
    }

    public CreateCard(CommandInput commandInput, boolean isOneTime) {
        super(commandInput);
        this.isOneTime = isOneTime;
    }

    @Override
    public void run() {
        if (Bank.getInstance().getActiveUser().getEmail().equals(commandInput.getEmail())) {
            String cardNumber = Utils.generateCardNumber();
            Database.getInstance().addCard(cardNumber,
                                            new Card(commandInput, cardNumber, isOneTime));
        } else {

        }
        //TODO STORE OUTPUT IN OBJECTNODE
    }
}
