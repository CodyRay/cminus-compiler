package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;





public class TypeSpecifierNode extends SyntaxTreeNode {
    public enum Type {
        Int,
        Void,
    }

    public Type type;

    public static TypeSpecifierNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        Type type;
        if(parser.parseTokenIf(TokenType.Int)) {
            type = Type.Int;
        } else if (parser.parseTokenIf(TokenType.Void)) {
            type = Type.Void;
        } else {
            parser.throwExpected(TokenType.Int, TokenType.Void);
            type = null; // Unreachable
        }
        
        return new TypeSpecifierNode(location, type);
    }

    public TypeSpecifierNode(Location location, Type type) {
    	super(location);
        this.type = type;
    }

    public String toString() {
    	if(type == Type.Int) {
    		return new Token(TokenType.Int).toString();    		
    	} else if (type == Type.Void) {
    		return new Token(TokenType.Void).toString();
    	} else {
    		return type.toString() + " ";
    	}
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.type)
			.result(this, other);
    }
}