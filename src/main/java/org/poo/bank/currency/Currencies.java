package org.poo.bank.currency;

import lombok.EqualsAndHashCode;

/**
 * This class is used as a Currency tuple.
 * @param <A> The first currency (usually String).
 * @param <B> The second currency (usually String).
 */
@EqualsAndHashCode
public final class Currencies<A, B> {

    private final A a;
    private final B b;

    /**
     * This constructor creates a Currency tuple instance.
     * @param a The first currency (usually String).
     * @param b The second currency (usually String).
     */
    public Currencies(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    /**
     * This method provides the first currency in the tuple.
     * @return The first currency.
     */
    public A getFirst() {
        return a;
    }

    /**
     * This method provides the second currency in the tuple.
     * @return The second currency.
     */
    public B getSecond() {
        return b;
    }
}
