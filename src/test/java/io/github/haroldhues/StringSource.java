package io.github.haroldhues;

public class StringSource extends Enumerable<Character> {
    private String source;
    private int position;

    public StringSource(String source) {
        this.source = source;
        position = -1;
    }

    public Character next() {
        position++;
        return position >= source.length() ? null : source.charAt(position);
    }
}