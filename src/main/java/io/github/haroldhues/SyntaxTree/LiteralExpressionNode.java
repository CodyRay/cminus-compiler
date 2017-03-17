package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class LiteralExpressionNode extends ExpressionNode {
    public Integer value;
    
    public static LiteralExpressionNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        Token token = parser.parseToken(TokenType.IntegerLiteral);
        Integer value = ((IntegerLiteralToken)token).value;

        return new LiteralExpressionNode(location, value);
    }
    public LiteralExpressionNode(Location location, Integer value) {
    	super(location);
        this.value = value;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Literal;
    }

    public String toAstString() {
        return buildAstString("LiteralExpressionNode", location.toAstString(), value.toString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new IntegerLiteralToken(value));
        return builder.toString();
    }

    public boolean equals(Object other) {
        return equalsBuilder(this)
            .property(o -> o.value)
            .result(this, other);
    }
}