package io.github.haroldhues;

public class Parser extends Enumerable<ParseTree>
{
    public Enumerable<Token> source;
    
    public Parser(Enumerable<Token> source) {
        this.source = source;
    }

    public Item next() {
        throw new UnsupportedOperationException();
    }
}