package org.poo.bank.output;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.commands.types.transactions.transactionHistory.TransactionData;

public class TransactionMessages {
    public static TransactionData withdrawSavings(double amount, String classicAccountIban,
                                                  String savingsAccountIban, String description,
                                                  int timestamp) {
        ObjectNode transactionInfo = Bank.getInstance().createObjectNode();
        transactionInfo.put("amount", amount);
        transactionInfo.put("classicAccountIban", classicAccountIban);
        transactionInfo.put("savingsAccountIban", savingsAccountIban);
        transactionInfo.put("description", description);
        transactionInfo.put("timestamp", timestamp);
        return new TransactionData(transactionInfo, savingsAccountIban);
    }

    public static TransactionData minimumAgeError(int timestamp, String accountIban) {
        ObjectNode transactionInfo = Bank.getInstance().createObjectNode();
        transactionInfo.put("description", "You don't have the minimum age required.");
        transactionInfo.put("timestamp", timestamp);
        return new TransactionData(transactionInfo, accountIban);
    }
}
