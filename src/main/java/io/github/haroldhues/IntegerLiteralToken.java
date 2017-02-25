package io.github.haroldhues;

public class IntegerLiteralToken extends Token
{
    public Integer value;
    
    public IntegerLiteralToken(Integer value) {
        super(TokenType.IntegerLiteral);
        this.value = value;
    }
}