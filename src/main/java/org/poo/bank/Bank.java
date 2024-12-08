package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.commands.CommandFactory;
import org.poo.bank.components.commerciants.CommerciantCategory;
import org.poo.bank.components.commerciants.Expenses;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.utils.Utils;

public final class Bank {
    private static final Bank BANK = new Bank();
    private ObjectMapper objectMapper;
    private ArrayNode output;

    private Bank() { }

    public static Bank getInstance() {
        return BANK;
    }

    public void runTest(final ObjectInput input,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        this.objectMapper = objectMapper;
        this.output = output;
        Utils.resetRandom();
        Database.getInstance().reset();
        Database.getInstance().addAll(input, this.objectMapper);
        CurrencyExchanger.reset();
        Expenses.reset();
        if (input.getCommerciants() != null) {
            for (CommerciantInput commerciantInput : input.getCommerciants()) {
                Expenses.addCategory(new CommerciantCategory(commerciantInput));
            }
        }
        for (ExchangeInput exchangeInput : input.getExchangeRates()) {
            CurrencyExchanger.addExchangeRate(exchangeInput);
        }
        for (CommandInput commandInput : input.getCommands()) {
            CommandFactory.generate(commandInput).run();
        }
    }

    public void addToOutput(final ObjectNode commandOutput) {
        output.add(commandOutput);
    }

    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }
}
