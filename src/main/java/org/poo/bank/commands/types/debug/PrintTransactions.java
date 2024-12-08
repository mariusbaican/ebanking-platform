package org.poo.bank.commands.types.debug;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class PrintTransactions extends Command {
    public PrintTransactions(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByUser(commandInput.getEmail());
        if (entry == null) {
            return;
        }

        output.put("command", "printTransactions");

        ArrayNode transactions = Bank.getInstance().createArrayNode();
        for (TransactionData transactionData : entry.getUser().getTransactionHistory()) {
            transactions.add(transactionData.getData());
        }

        output.put("output", transactions);
        output.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(output);
    }
}
