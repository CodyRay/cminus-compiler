package io.github.haroldhues;

public class AstBuilder extends Enumerable<AbstractSyntaxTree>
{
    public Enumerable<ParseTree> source;
    public AstBuilder(Enumerable<ParseTree> source) {
        this.source = source;
    }

    public Item next() {
        throw new UnsupportedOperationException();
    }
}