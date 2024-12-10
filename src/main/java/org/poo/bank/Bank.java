package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.commands.CommandFactory;
import org.poo.bank.currency.CurrencyExchanger;
import org.poo.bank.database.Database;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.utils.Utils;

/**
 * This class stores the Bank's information and handles the operations.
 * It uses the Singleton design pattern.
 */
public final class Bank {
    private static final Bank BANK = new Bank();
    @Getter
    private final Database database;
    private ObjectMapper mapper;
    private ArrayNode output;

    private Bank() {
        database = new Database();
    }

    /**
     * This method provides the unique and globally available Bank instance.
     * @return The unique Bank instance.
     */
    public static Bank getInstance() {
        return BANK;
    }

    /**
     * This method runs the Bank's operations. It adds Users to the Database,
     * stores ExchangeRates and runs through the list of Commands.
     * @param input The input for the operations.
     * @param objectMapper The objectMapper used to create output nodes.
     * @param globalOutput The output for messages to be added to.
     */
    public void runOperations(final ObjectInput input,
                        final ObjectMapper objectMapper,
                        final ArrayNode globalOutput) {
        this.mapper = objectMapper;
        this.output = globalOutput;
        Utils.resetRandom();
        database.reset();
        database.addUsers(input.getUsers());
        CurrencyExchanger.reset();
        for (ExchangeInput exchangeInput : input.getExchangeRates()) {
            CurrencyExchanger.addExchangeRate(exchangeInput);
        }
        for (CommandInput commandInput : input.getCommands()) {
            CommandFactory.generate(commandInput).run();
        }
    }

    /**
     * This method adds an ObjectNode to the global output.
     * @param commandOutput The ObjectNode to be added.
     */
    public void addToOutput(final ObjectNode commandOutput) {
        output.add(commandOutput);
    }

    /**
     * This method creates an ObjectNode using the ObjectMapper.
     * @return A new ObjectNode.
     */
    public ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    /**
     * This method creates an ArrayNode using the ObjectMapper.
     * @return A new ArrayNode.
     */
    public ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

}
