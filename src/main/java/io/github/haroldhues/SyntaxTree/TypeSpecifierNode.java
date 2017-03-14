package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;





public class TypeSpecifierNode extends SyntaxTreeNode {
    public enum Type {
        Int,
        Void,
    }

    public Type type;

    public static TypeSpecifierNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        Type type;
        if(parser.parseTokenIf(TokenType.Int)) {
            type = Type.Int;
        } else if (parser.parseTokenIf(TokenType.Void)) {
            type = Type.Void;
        } else {
            parser.throwExpected(TokenType.Int, TokenType.Void);
            type = null; // Unreachable
        }
        
        TypeSpecifierNode node = new TypeSpecifierNode(type);
        visitor.accept(node);
        return node;
    }

    public TypeSpecifierNode(Type type) {
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