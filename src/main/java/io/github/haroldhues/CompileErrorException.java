package io.github.haroldhues;
public class CompileErrorException extends Exception {
    
    private static final long serialVersionUID = 2313464649294894016L;

    public CompileErrorException(String message, Location location) {
        super(location.toString() + ": " + message);
    }
    public CompileErrorException(String message) {
        super(message);
    }
}
