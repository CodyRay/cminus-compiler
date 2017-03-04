package io.github.haroldhues.Tokens;

public class IntegerLiteralToken extends Token
{
    public Integer value;
    
    public IntegerLiteralToken(Integer value) {
        super(TokenType.IntegerLiteral);
        this.value = value;
    }

    public IntegerLiteralToken(Integer value, int line, int column) {
        super(TokenType.IntegerLiteral, line, column);
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