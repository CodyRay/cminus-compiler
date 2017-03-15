package io.github.haroldhues;

public class Location
{
    private int line;
    private int column;

    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    private Location() {}

    public static final Location None = new NoLocation();

    public Integer getLine() {
        return line;
    }

    public Integer getColumn() {
        return column;
    }

    public String toString() {
        return Integer.toString(line) + ":" + Integer.toString(column);
    }

    private static class NoLocation extends Location {
        public Integer getLine() {
            return null;
        }

        public Integer getColumn() {
            return null;
        }

        public String toString() {
            return "<>";
        }
    }
}
