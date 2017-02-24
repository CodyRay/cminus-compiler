package io.github.haroldhues;

public class StringSource extends Enumerable<Character> {
    private String source;
    private int position;

    public StringSource(String source) {
        this.source = source;
        position = -1;
    }

    public Item next() {
        position++;
        return position >= source.length() ? new Item() : new Item(source.charAt(position));
    }
}