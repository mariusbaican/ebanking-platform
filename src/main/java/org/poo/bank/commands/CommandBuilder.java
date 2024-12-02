package org.poo.bank.commands;

import org.poo.bank.commands.types.transactions.*;
import org.poo.fileio.CommandInput;

public class CommandBuilder {
    public static Command build(CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers" -> {
                return null;
            }
            case "printTransactions" -> {
                return null;
            }
            case "openAccount" -> {
                return new OpenAccount(commandInput);
            }
            case "addFunds" -> {
                return new AddFunds(commandInput);
            }
            case "createCard" -> {
                return new CreateCard(commandInput);
            }
            case "createOneTimeCard" -> {
                return new CreateCard(commandInput, true);
            }
            case "deleteAccount" -> {
                return new DeleteAccount(commandInput);
            }
            case "deleteCard" -> {
                return new DeleteCard(commandInput);
            }
            case "setMinBalance" -> {
                return new SetMinBalance(commandInput);
            }
            case "checkCardStatus" -> {
                return new CheckCardStatus(commandInput);
            }
            case "payOnline" -> {
                return new PayOnline(commandInput); //TODO FINISH IMPLEMENTING THIS
            }
            case "sendMoney" -> {
                return null;
            }
            case "setAlias" -> {
                return new SetAlias(commandInput);
            }
            case "splitPayment" -> {
                return null;
            }
            case "addInterest" -> {
                return null;
            }
            case "changeInterestRate" -> {
                return null;
            }
            case "report" -> {
                return null;
            }
            case "spendingReport" -> {
                return null;
            }
            default ->
                throw new IllegalArgumentException("Unknown transaction: " + commandInput.getCommand());
        }
    }
}
