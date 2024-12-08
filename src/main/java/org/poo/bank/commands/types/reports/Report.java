package org.poo.bank.commands.types.reports;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.Command;
import org.poo.bank.components.TransactionData;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.fileio.CommandInput;

public final class Report extends Command {
    public Report(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void run() {
        DatabaseEntry entry = Database.getInstance().getEntryByAccount(commandInput.getAccount());
        if (entry == null) {
            ObjectNode commandOutput = Bank.getInstance().createObjectNode();
            commandOutput.put("command", "report");

            output.put("description", "Account not found");
            output.put("timestamp", commandInput.getTimestamp());

            commandOutput.put("output", output);
            commandOutput.put("timestamp", commandInput.getTimestamp());
            Bank.getInstance().addToOutput(commandOutput);
            return;
        }

        ObjectNode report = Bank.getInstance().createObjectNode();
        report.put("command", "report");

        ObjectNode accountInfo = Bank.getInstance().createObjectNode();
        accountInfo.put("IBAN", commandInput.getAccount());
        accountInfo.put("balance",
                entry.getAccounts().get(commandInput.getAccount()).getBalance());
        accountInfo.put("currency",
                entry.getAccounts().get(commandInput.getAccount()).getCurrency());

        ArrayNode transactions = Bank.getInstance().createArrayNode();
        for (TransactionData data : entry.getUser().getTransactionHistory()) {
            if (data.getAccount().equals(commandInput.getAccount())) {
                if (data.getData().get("timestamp").asInt() >= commandInput.getStartTimestamp()
                && data.getData().get("timestamp").asInt() <= commandInput.getEndTimestamp()) {
                    transactions.add(data.getData());
                }
            }
        }
        accountInfo.put("transactions", transactions);

        report.put("output", accountInfo);
        report.put("timestamp", commandInput.getTimestamp());
        Bank.getInstance().addToOutput(report);
    }
}
