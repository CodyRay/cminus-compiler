package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class VariableExpressionNode extends ExpressionNode {
    public String identifier;
    public ExpressionNode arrayExpression;
    
    public static VariableExpressionNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        IdentifierToken idToken = (IdentifierToken)parser.parseToken(TokenType.Identifier);
        String identifier = idToken.identifier;
        ExpressionNode arrayExpression = parseArrayNotation(parser, visitor);

        VariableExpressionNode expression = new VariableExpressionNode(identifier, arrayExpression);
        visitor.accept(expression);
        return expression;
    }

    public VariableExpressionNode(String identifier, ExpressionNode arrayExpression) {
        this.identifier = identifier;
        this.arrayExpression = arrayExpression;
    }

    public VariableExpressionNode(String identifier) {
        this.identifier = identifier;
        this.arrayExpression = null;
    }

    public static ExpressionNode parseArrayNotation(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        ExpressionNode returnValue = null;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            returnValue = ExpressionNode.parse(parser, visitor);
            parser.parseToken(TokenType.RightBracket);
        }
        return returnValue;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Variable;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(arrayExpression, visitor);
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(arrayExpression != null) {
            builder.append(identifier);
            builder.append(new Token(TokenType.LeftBracket));
            builder.append(arrayExpression);
            builder.append(new Token(TokenType.RightBracket));
        } else {
            builder.append(new IdentifierToken(identifier));
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