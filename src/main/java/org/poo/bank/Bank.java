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
    @Getter
    private final CurrencyExchanger currencyExchanger;
    private ObjectMapper mapper;
    private ArrayNode globalOutput;

    private Bank() {
        database = new Database();
        currencyExchanger = new CurrencyExchanger();
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
     * @param output The output for messages to be added to.
     */
    public void runOperations(final ObjectInput input,
                        final ObjectMapper objectMapper,
                        final ArrayNode output) {
        this.mapper = objectMapper;
        this.globalOutput = output;
        Utils.resetRandom();
        database.reset();
        database.addUsers(input.getUsers());
        currencyExchanger.reset();
        for (ExchangeInput exchangeInput : input.getExchangeRates()) {
            currencyExchanger.addExchangeRate(exchangeInput);
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
        globalOutput.add(commandOutput);
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

    // This might be unused, but it's here just for the sake of having error
    // outputs for everything
    /**
     * This method provides a JSON format output for a non-existent user.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the error message.
     */
    public ObjectNode userNotFoundJson(final int timestamp) {
        ObjectNode output = createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "User not found");

        return output;
    }

    /**
     * This method provides a JSON format output for a non-existent account.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the error message.
     */
    public ObjectNode accountNotFoundJson(final int timestamp) {
        ObjectNode output = createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "Account not found");

        return output;
    }

    /**
     * This method provides a JSON format output for a non-existent card.
     * @param timestamp The timestamp of the request.
     * @return An ObjectNode containing the error message.
     */
    public ObjectNode cardNotFoundJson(final int timestamp) {
        ObjectNode output = createObjectNode();
        output.put("timestamp", timestamp);
        output.put("description", "Card not found");

        return output;
    }

}
