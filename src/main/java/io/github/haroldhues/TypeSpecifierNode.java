package io.github.haroldhues;

public class TypeSpecifierNode extends SyntaxTree {
    public enum Type {
        Int,
        Void,
    }

    public Type type;

    public TypeSpecifierNode(Parser parser) throws Exception {
        if(parser.parseTokenIf(TokenType.Int)) {
            type = Type.Int;
        } else if (parser.parseTokenIf(TokenType.Void)) {
            type = Type.Void;
        } else {
            parser.throwExpected(TokenType.Int, TokenType.Void);
        }
        parser.visit(this);
    }
}