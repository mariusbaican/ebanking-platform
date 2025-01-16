package org.poo.bank.output.visitor;

public interface Visitable {
    <T> T accept(OutputVisitor<T> outputVisitor);
}
