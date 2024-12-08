package org.poo.bank.currency;

import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.Map;

public class CurrencyExchanger {
    public static HashMap<Currencies<String, String>, Double> exchangeRates = new HashMap<>();

    public static void reset() {
        exchangeRates.clear();
    }

    public static void addExchangeRate(ExchangeInput input) {
        exchangeRates.putIfAbsent(new Currencies<>(input.getFrom(), input.getTo()), input.getRate());
        exchangeRates.putIfAbsent(new Currencies<>(input.getTo(), input.getFrom()), 1 / input.getRate());
        computeMissingRates(new Currencies<>(input.getFrom(), input.getTo()), input.getRate());
    }

    public static void addExchangeRate(Currencies<String, String> currencies, double rate) {
        exchangeRates.putIfAbsent(currencies, rate);
        exchangeRates.putIfAbsent(new Currencies<>(currencies.getSecond(), currencies.getFirst()), 1 / rate);
    }

    private static void computeMissingRates(Currencies<String, String> newCurrencies, double rate) {
        Map<Currencies<String, String>, Double> newRates = new HashMap<>();

        for (Currencies<String, String> existingCurrencies : exchangeRates.keySet()) {
            String existingStart = existingCurrencies.getFirst();
            String existingEnd = existingCurrencies.getSecond();

            if (existingEnd.equals(newCurrencies.getFirst())) {
                double newRate = exchangeRates.get(existingCurrencies) * rate;
                Currencies<String, String> newRatePair = new Currencies<>(existingStart, newCurrencies.getSecond());
                if (!exchangeRates.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }

            if (newCurrencies.getSecond().equals(existingStart)) {
                double newRate = rate * exchangeRates.get(existingCurrencies);
                Currencies<String, String> newRatePair = new Currencies<>(newCurrencies.getFirst(), existingEnd);
                if (!exchangeRates.containsKey(newRatePair)) {
                    newRates.put(newRatePair, newRate);
                }
            }
        }

        for (Currencies<String, String> currencies : newRates.keySet()) {
            addExchangeRate(currencies, newRates.get(currencies));
        }
    }

    public static double getRate(Currencies<String, String> currencies) {
        if (currencies.getFirst().equals(currencies.getSecond())) {
            return 1.0;
        }
        double rate = exchangeRates.getOrDefault(currencies, -1.0);
        if (Double.compare(rate, -1.0) == 0) {
            throw new IllegalArgumentException("No rate found for " + currencies.getFirst() + " to " + currencies.getSecond());
        }
        return rate;

    }
}
