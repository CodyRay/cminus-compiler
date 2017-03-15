package io.github.haroldhues.Tokens;

import io.github.haroldhues.Location;



public class IdentifierToken extends Token
{
    public String identifier;
    
    public IdentifierToken(String identifier) {
        super(TokenType.Identifier);
        this.identifier = identifier;
    }
    
    public IdentifierToken(String identifier, Location location) {
        super(TokenType.Identifier, location);
        this.identifier = identifier;
    }

    public String toString() {
        return identifier + " ";
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof IdentifierToken)) {
            return false;
        }
         
        IdentifierToken that = (IdentifierToken) other;
 
        return this.type.equals(that.type) && this.identifier.equals(that.identifier);
    }
}