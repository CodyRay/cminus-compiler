package io.github.haroldhues;

public class Scanner extends Enumerable<Token>
{
    public Enumerable<Character> source;
    public Scanner(Enumerable<Character> source) {
        this.source = source;
    }

    public Item next() {
        throw new UnsupportedOperationException();
    }
}
