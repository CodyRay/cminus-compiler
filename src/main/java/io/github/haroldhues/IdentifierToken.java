package io.github.haroldhues;

public class IdentifierToken extends Token
{
    public String identifier;
    
    public IdentifierToken(String identifier) {
        super(TokenType.Identifier);
        this.identifier = identifier;
    }
}