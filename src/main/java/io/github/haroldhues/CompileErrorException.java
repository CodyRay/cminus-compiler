package io.github.haroldhues;
public class CompileErrorException extends Exception {
    
    private static final long serialVersionUID = 2313464649294894016L;

    public CompileErrorException(String message, int line, int column) {
        super(Integer.toString(line) + ":" + Integer.toString(column) + ": " + message);
    }
    public CompileErrorException(String message) {
        super(message);
    }
}
