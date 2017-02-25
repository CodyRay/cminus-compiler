package io.github.haroldhues;

public class Token 
{
    public TokenType type;
    
    public Token(TokenType type) {
        this.type = type;
    }

    public Token(String text) {
        throw new UnsupportedOperationException();
    }
}