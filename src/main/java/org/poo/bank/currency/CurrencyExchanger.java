package org.poo.bank.currency;

import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.Map;

public final class CurrencyExchanger {
    public static final HashMap<Currencies<String, String>, Double> EXCHANGE_RATES =
            new HashMap<>();

    public static void reset() {
        EXCHANGE_RATES.clear();
    }

    public static void addExchangeRate(final ExchangeInput input) {
        EXCHANGE_RATES.putIfAbsent(new Currencies<>(input.getFrom(), input.getTo()),
                                    input.getRate());
        EXCHANGE_RATES.putIfAbsent(new Currencies<>(input.getTo(), input.getFrom()),
                                    1 / input.getRate());
        computeMissingRates(new Currencies<>(input.getFrom(), input.getTo()), input.getRate());
    }

    public static void addExchangeRate(final Currencies<String, String> currencies,
                                       final double rate) {
        EXCHANGE_RATES.putIfAbsent(currencies, rate);
        EXCHANGE_RATES.putIfAbsent(new Currencies<>(currencies.getSecond(), currencies.getFirst()),
                                    1 / rate);
    }

    private static void computeMissingRates(final Currencies<String, String> newCurrencies,
                                            final double rate) {
        Map<Currencies<String, String>, Double> newRates = new HashMap<>();

        for (Currencies<String, String> existingCurrencies : EXCHANGE_RATES.keySet()) {
            String existingStart = existingCurrencies.getFirst();
            String existingEnd = existingCurrencies.getSecond();

            if (existingEnd.equals(newCurrencies.getFirst())) {
                double newRate = EXCHANGE_RATES.get(existingCurrencies) * rate;
                Currencies<String, String> newRatePair =
                        new Currencies<>(existingStart, newCurrencies.getSecond());
                if (!EXCHANGE_RATES.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }

            if (newCurrencies.getSecond().equals(existingStart)) {
                double newRate = rate * EXCHANGE_RATES.get(existingCurrencies);
                Currencies<String, String> newRatePair =
                        new Currencies<>(newCurrencies.getFirst(), existingEnd);
                if (!EXCHANGE_RATES.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }
        }

        for (Currencies<String, String> currencies : newRates.keySet()) {
            addExchangeRate(currencies, newRates.get(currencies));
        }
    }

    public static double getRate(final Currencies<String, String> currencies) {
        if (currencies.getFirst().equals(currencies.getSecond())) {
            return 1.0;
        }
        double rate = EXCHANGE_RATES.getOrDefault(currencies, -1.0);
        if (Double.compare(rate, -1.0) == 0) {
            throw new IllegalArgumentException("No rate found for "
                                                + currencies.getFirst() + " to "
                                                + currencies.getSecond());
        }
        return rate;

    }
}
