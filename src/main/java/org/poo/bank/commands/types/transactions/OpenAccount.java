package org.poo.bank.commands.types.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.accounts.Account;
import org.poo.bank.components.AccountBuilder;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class OpenAccount extends Command {
    public OpenAccount(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        Account account = AccountBuilder.build(commandInput, Utils.generateIBAN());
        Database.getInstance().addAccount(commandInput.getEmail(), account);
        //TODO STORE OUTPUT IN OBJECTNODE
    }
}
