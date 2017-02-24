package io.github.haroldhues;

public class IdentifierToken extends Token
{
    public String identifier;
    
    public IdentifierToken(TokenType type, String identifier) {
        super(type);
        this.identifier = identifier;
    }
}