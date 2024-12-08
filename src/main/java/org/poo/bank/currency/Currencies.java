package org.poo.bank.currency;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Currencies<A, B> {

    private final A a;
    private final B b;

    public Currencies(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }
}
