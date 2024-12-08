package org.poo.bank.commands.types.debug;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.components.User;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;

public class PrintTransactions extends Command {
    public PrintTransactions(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        User user = Database.getInstance().getUser(commandInput.getEmail());
        if (user == null)
            return;

        output.put("command", "printTransactions");
        ArrayNode transactions = Bank.getInstance().getObjectMapper().createArrayNode();
        for (TransactionData transactionData : user.getTransactionHistory()) {
            transactions.add(transactionData.getData());
        }
        output.put("output", transactions);
        output.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().getOutput().add(output);
    }
}
