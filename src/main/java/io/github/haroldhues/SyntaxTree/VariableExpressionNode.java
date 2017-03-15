package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class VariableExpressionNode extends ExpressionNode {
    public String identifier;
    public ExpressionNode arrayExpression;
    
    public static VariableExpressionNode parse(Parser parser) throws CompileErrorException {
        IdentifierToken idToken = (IdentifierToken)parser.parseToken(TokenType.Identifier);
        String identifier = idToken.identifier;
        ExpressionNode arrayExpression = parseArrayNotation(parser);

        VariableExpressionNode expression = new VariableExpressionNode(parser.currentLocation(), identifier, arrayExpression);
        return expression;
    }

    public VariableExpressionNode(Location location, String identifier, ExpressionNode arrayExpression) {
    	super(location);
        this.identifier = identifier;
        this.arrayExpression = arrayExpression;
    }

    public VariableExpressionNode(Location location, String identifier) {
    	super(location);
        this.identifier = identifier;
        this.arrayExpression = null;
    }

    public static ExpressionNode parseArrayNotation(Parser parser) throws CompileErrorException {
        ExpressionNode returnValue = null;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            returnValue = ExpressionNode.parse(parser);
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