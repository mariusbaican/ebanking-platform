package org.poo.bank.currency;

import org.poo.bank.Tuple;
import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a utility class used to convert currencies.
 */
public final class CurrencyExchanger {
    public static final HashMap<Tuple<String, String>, Double> EXCHANGE_RATES =
            new HashMap<>();

    public CurrencyExchanger() { }

    /**
     * This method is used to reset the EXCHANGE_RATES Map.
     */
    public void reset() {
        EXCHANGE_RATES.clear();
    }

    /**
     * This method is used to add a new exchangeRate to the Map.
     * Additionally, it computes all the rates that are possible to be determined.
     * @param input The desired ExchangeInput.
     */
    public void addExchangeRate(final ExchangeInput input) {
        EXCHANGE_RATES.putIfAbsent(new Tuple<>(input.getFrom(), input.getTo()),
                                    input.getRate());
        EXCHANGE_RATES.putIfAbsent(new Tuple<>(input.getTo(), input.getFrom()),
                                    1 / input.getRate());
        computeMissingRates(new Tuple<>(input.getFrom(), input.getTo()), input.getRate());
    }

    /**
     * This method is used to add a new exchangeRate to the Map.
     * It does not compute additional rates.
     * @param tuple A Currency tuple.
     * @param rate An exchangeRate.
     */
    public void addExchangeRate(final Tuple<String, String> tuple,
                                       final double rate) {
        EXCHANGE_RATES.putIfAbsent(tuple, rate);
        EXCHANGE_RATES.putIfAbsent(new Tuple<>(tuple.getSecond(), tuple.getFirst()),
                                    1 / rate);
    }

    /**
     * This method is used to compute missing exchangeRates.
     * @param newTuple The Currency tuple newly added.
     * @param rate The new exchangeRate added.
     */
    private void computeMissingRates(final Tuple<String, String> newTuple,
                                            final double rate) {
        Map<Tuple<String, String>, Double> newRates = new HashMap<>();

        for (Tuple<String, String> existingTuple : EXCHANGE_RATES.keySet()) {
            String existingStart = existingTuple.getFirst();
            String existingEnd = existingTuple.getSecond();

            if (existingEnd.equals(newTuple.getFirst())) {
                double newRate = EXCHANGE_RATES.get(existingTuple) * rate;
                Tuple<String, String> newRatePair =
                        new Tuple<>(existingStart, newTuple.getSecond());
                if (!EXCHANGE_RATES.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }

            if (newTuple.getSecond().equals(existingStart)) {
                double newRate = rate * EXCHANGE_RATES.get(existingTuple);
                Tuple<String, String> newRatePair =
                        new Tuple<>(newTuple.getFirst(), existingEnd);
                if (!EXCHANGE_RATES.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }
        }

        for (Tuple<String, String> tuple : newRates.keySet()) {
            addExchangeRate(tuple, newRates.get(tuple));
        }
    }

    /**
     * This method provides the exchangeRate between two Currencies.
     * @param tuple The currency tuple to transfer from and to.
     * @return The exchangeRate.
     */
    private double getRate(final Tuple<String, String> tuple) {
        if (tuple.getFirst().equals(tuple.getSecond())) {
            return 1.0;
        }
        double rate = EXCHANGE_RATES.getOrDefault(tuple, -1.0);
        if (Double.compare(rate, -1.0) == 0) {
            throw new IllegalArgumentException("No rate found for "
                                                + tuple.getFirst() + " to "
                                                + tuple.getSecond());
        }
        return rate;
    }

    public double exchange(final Tuple<String, String> tuple, double sum) {
        double rate = getRate(tuple);
        return sum * rate;
    }
}
