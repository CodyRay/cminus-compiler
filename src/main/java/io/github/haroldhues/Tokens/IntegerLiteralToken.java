package io.github.haroldhues.Tokens;

import io.github.haroldhues.Location;



public class IntegerLiteralToken extends Token
{
    public Integer value;
    
    public IntegerLiteralToken(Integer value) {
        super(TokenType.IntegerLiteral);
        this.value = value;
    }

    public IntegerLiteralToken(Integer value, Location location) {
        super(TokenType.IntegerLiteral, location);
        this.value = value;
    }

    public String toString() {
        return value.toString() + " ";
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof IntegerLiteralToken)) {
            return false;
        }
         
        IntegerLiteralToken that = (IntegerLiteralToken) other;
 
        return this.type.equals(that.type) && this.value.equals(that.value);
    }
}