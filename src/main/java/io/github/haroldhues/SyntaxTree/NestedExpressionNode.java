package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.Location;
import io.github.haroldhues.Tokens.*;

public class NestedExpressionNode extends ExpressionNode {
    public ExpressionNode expression;

    public NestedExpressionNode(Location location, ExpressionNode expression) {
    	super(location);
        this.expression = expression;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Nested;
    }

    public String toAstString() {
        return buildAstString("NestedExpressionNode", location.toAstString(), expression.toAstString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.LeftParenthesis));
        builder.append(expression);
        builder.append(new Token(TokenType.RightParenthesis));
        return builder.toString();
    }

    public boolean equals(Object other) {
        return equalsBuilder(this)
            .property(o -> o.expression)
            .result(this, other);
    }
}