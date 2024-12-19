package org.poo.bank.commands.output.visitor;

public interface Visitable {
    <T> T accept(OutputVisitor<T> outputVisitor);
}
