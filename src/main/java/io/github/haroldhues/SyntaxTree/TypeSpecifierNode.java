package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;





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
}