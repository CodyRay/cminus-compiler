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

    public TypeSpecifierNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        if(parser.parseTokenIf(TokenType.Int)) {
            type = Type.Int;
        } else if (parser.parseTokenIf(TokenType.Void)) {
            type = Type.Void;
        } else {
            parser.throwExpected(TokenType.Int, TokenType.Void);
        }
        
        visitor.accept(this);
    }

    public TypeSpecifierNode(Type type) {
        this.type = type;
    }

    public String toString() {
        return new Token(TokenType.Int).toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof TypeSpecifierNode)) {
            return false;
        }
         
        TypeSpecifierNode that = (TypeSpecifierNode) other;
 
        return this.type != that.type;
    }
}