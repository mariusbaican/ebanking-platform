package org.poo.bank.commands;

import org.poo.bank.commands.types.debug.PrintUsers;
import org.poo.bank.commands.types.transactions.*;
import org.poo.fileio.CommandInput;

public abstract class CommandBuilder {
    public static Command build(CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers" -> {
                return new PrintUsers(commandInput);
            }
            case "printTransactions" -> {
                return null;
            }
            case "addAccount" -> {
                return new AddAccount(commandInput);
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
            case "setMinimumBalance" -> {
                return new SetMinBalance(commandInput);
            }
            case "checkCardStatus" -> {
//                return new CheckCardStatus(commandInput);
            }
            case "payOnline" -> {
//                return new PayOnline(commandInput); //TODO IMPLEMENT CURRENCY EXCHANGE FIRST
            }
            case "sendMoney" -> {
//                return new SendMoney(commandInput);
            }
            case "setAlias" -> {
//                return new SetAlias(commandInput);
            }
            case "splitPayment" -> {
//                return new SplitPayment(commandInput); //TODO IMPLEMENT CURRENCY EXCHANGE FIRST
            }
            case "addInterest" -> {
                return new AddInterest(commandInput);
            }
            case "changeInterestRate" -> {
                return new ChangeInterestRate(commandInput);
            }
            case "report" -> {
                return null;
            }
            case "spendingsReport" -> {
                return null;
            }
            default ->
                throw new IllegalArgumentException("Unknown transaction: " + commandInput.getCommand());
        }
        return null;
    }
}
