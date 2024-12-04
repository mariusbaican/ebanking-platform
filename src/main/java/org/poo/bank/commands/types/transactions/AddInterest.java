package org.poo.bank.commands.types.transactions;

import org.poo.bank.commands.Command;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class AddInterest extends Command {
    public AddInterest(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        if (Database.getInstance().getAccount(commandInput.getAccount()) == null)
            return;
        boolean ret = Database.getInstance().getAccount(commandInput.getAccount()).addInterest();
        if (ret) {

        } else {

        }
        //TODO ADD OUTPUT TO JSON
    }
}
