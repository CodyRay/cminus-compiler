package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class VariableNode extends SyntaxTreeNode {
    public String identifier;
    public ExpressionNode arrayExpression;
    
    public VariableNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        IdentifierToken idToken = (IdentifierToken)parser.parseToken(TokenType.Identifier);
        identifier = idToken.identifier;
        arrayExpression = parseArrayNotation(parser, visitor);

        visitor.accept(this);
    }

    public VariableNode(String identifier, ExpressionNode arrayExpression) {
        this.identifier = identifier;
        this.arrayExpression = arrayExpression;
    }

    public VariableNode(String identifier) {
        this.identifier = identifier;
    }

    public static ExpressionNode parseArrayNotation(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        ExpressionNode returnValue = null;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            returnValue = new ExpressionNode(parser, visitor);
            parser.parseToken(TokenType.RightBracket);
        }
        return returnValue;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new IdentifierToken(identifier));
        if(arrayExpression != null) {
            builder.append(new Token(TokenType.LeftBracket));
            builder.append(arrayExpression);
            builder.append(new Token(TokenType.RightBracket));
        }
        return builder.toString();
    }

    public boolean equals(Object other) {
        return equalsBuilder(this)
            .property(o -> o.identifier)
            .property(o -> o.arrayExpression)
            .result(this, other);
    }
}