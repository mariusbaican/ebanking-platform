package org.poo.bank.commands;

import org.poo.bank.commands.types.debug.PrintTransactions;
import org.poo.bank.commands.types.debug.PrintUsers;
import org.poo.bank.commands.types.reports.CreateBusinessReport;
import org.poo.bank.commands.types.reports.CreateAccountReport;
import org.poo.bank.commands.types.reports.CreateSpendingReport;
import org.poo.bank.commands.types.transactions.AddAccount;
import org.poo.bank.commands.types.transactions.AddFunds;
import org.poo.bank.commands.types.transactions.AddInterest;
import org.poo.bank.commands.types.transactions.AddNewBusinessAssociate;
import org.poo.bank.commands.types.transactions.CashWithdrawal;
import org.poo.bank.commands.types.transactions.ChangeDepositLimit;
import org.poo.bank.commands.types.transactions.ChangeInterestRate;
import org.poo.bank.commands.types.transactions.ChangeSpendingLimit;
import org.poo.bank.commands.types.transactions.CheckCardStatus;
import org.poo.bank.commands.types.transactions.CreateCard;
import org.poo.bank.commands.types.transactions.CreateOneTimeCard;
import org.poo.bank.commands.types.transactions.DeleteAccount;
import org.poo.bank.commands.types.transactions.DeleteCard;
import org.poo.bank.commands.types.transactions.PayOnline;
import org.poo.bank.commands.types.transactions.SendMoney;
import org.poo.bank.commands.types.transactions.SetAlias;
import org.poo.bank.commands.types.transactions.SetMinimumBalance;
import org.poo.bank.commands.types.transactions.SplitPayment;
import org.poo.bank.commands.types.transactions.WithdrawSavings;
import org.poo.fileio.CommandInput;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This static class is used to generate every command request.
 * It implements the Factory design pattern.
 */
public final class CommandFactory {
    private static final Map<String, Function<CommandInput, Command>> COMMANDS = new HashMap<>();

    static {
        register("printUsers", PrintUsers::new);
        register("printTransactions", PrintTransactions::new);
        register("report", CreateAccountReport::new);
        register("spendingsReport", CreateSpendingReport::new);
        register("businessReport", CreateBusinessReport::new);
        register("addAccount", AddAccount::new);
        register("addFunds", AddFunds::new);
        register("addInterest", AddInterest::new);
        register("addNewBusinessAssociate", AddNewBusinessAssociate::new);
        register("cashWithdrawal", CashWithdrawal::new);
        register("changeDepositLimit", ChangeDepositLimit::new);
        register("changeInterestRate", ChangeInterestRate::new);
        register("changeSpendingLimit", ChangeSpendingLimit::new);
        register("checkCardStatus", CheckCardStatus::new);
        register("createCard", CreateCard::new);
        register("createOneTimeCard", CreateOneTimeCard::new);
        register("deleteAccount", DeleteAccount::new);
        register("deleteCard", DeleteCard::new);
        register("payOnline", PayOnline::new);
        register("sendMoney", SendMoney::new);
        register("setAlias", SetAlias::new);
        register("setMinimumBalance", SetMinimumBalance::new);
        register("splitPayment", SplitPayment::new);
        register("withdrawSavings", WithdrawSavings::new);
    }

    private static void register(String commandName, Function<CommandInput, Command> constructor) {
        COMMANDS.put(commandName, constructor);
    }

    private CommandFactory() { }

    /**
     * This method generates a command based on a given input.
     * @param commandInput The general input of a command.
     * @return A command subclass that fulfills the desired task.
     */
    public static Command generate(final CommandInput commandInput) {
        return COMMANDS.get(commandInput.getCommand()).apply(commandInput);
    }
}
