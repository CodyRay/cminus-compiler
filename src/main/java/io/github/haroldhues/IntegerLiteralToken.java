package io.github.haroldhues;

public class IntegerLiteralToken extends Token
{
    public Integer value;
    
    public IntegerLiteralToken(TokenType type, Integer value) {
        super(type);
        this.value = value;
    }
}