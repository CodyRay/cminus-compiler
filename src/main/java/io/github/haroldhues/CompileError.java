package io.github.haroldhues;

public class CompileError
{
    public String message;
    public CompileError(String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
