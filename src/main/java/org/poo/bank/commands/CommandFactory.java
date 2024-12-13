package org.poo.bank.commands;

import org.poo.bank.commands.types.debug.PrintTransactions;
import org.poo.bank.commands.types.debug.PrintUsers;
import org.poo.bank.commands.types.reports.Report;
import org.poo.bank.commands.types.reports.SpendingsReport;
import org.poo.bank.commands.types.transactions.AddAccount;
import org.poo.bank.commands.types.transactions.AddFunds;
import org.poo.bank.commands.types.transactions.AddInterest;
import org.poo.bank.commands.types.transactions.ChangeInterestRate;
import org.poo.bank.commands.types.transactions.CheckCardStatus;
import org.poo.bank.commands.types.transactions.CreateCard;
import org.poo.bank.commands.types.transactions.DeleteAccount;
import org.poo.bank.commands.types.transactions.DeleteCard;
import org.poo.bank.commands.types.transactions.PayOnline;
import org.poo.bank.commands.types.transactions.SendMoney;
import org.poo.bank.commands.types.transactions.SetAlias;
import org.poo.bank.commands.types.transactions.SetMinimumBalance;
import org.poo.bank.commands.types.transactions.SplitPayment;
import org.poo.fileio.CommandInput;

/**
 * This static class is used to generate every command request.
 * It implements the Factory design pattern.
 */
public final class CommandFactory {
    private CommandFactory() { }

    /**
     * This method generates a command based on a given input.
     * @param commandInput The general input of a command.
     * @return A command subclass that fulfills the desired task.
     */
    public static Command generate(final CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers" -> {
                return new PrintUsers(commandInput);
            }
            case "printTransactions" -> {
                return new PrintTransactions(commandInput);
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
                return new SetMinimumBalance(commandInput);
            }
            case "checkCardStatus" -> {
                return new CheckCardStatus(commandInput);
            }
            case "payOnline" -> {
                return new PayOnline(commandInput);
            }
            case "sendMoney" -> {
                return new SendMoney(commandInput);
            }
            case "setAlias" -> {
                return new SetAlias(commandInput);
            }
            case "splitPayment" -> {
                return new SplitPayment(commandInput);
            }
            case "addInterest" -> {
                return new AddInterest(commandInput);
            }
            case "changeInterestRate" -> {
                return new ChangeInterestRate(commandInput);
            }
            case "report" -> {
                return new Report(commandInput);
            }
            case "spendingsReport" -> {
                return new SpendingsReport(commandInput);
            }
            default ->
                throw new IllegalArgumentException("Unknown transaction: "
                                                    + commandInput.getCommand());
        }
    }
}
