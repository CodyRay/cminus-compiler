package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class LiteralExpressionNode extends ExpressionNode {
    public Integer value;
    
    public static LiteralExpressionNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        Token token = parser.parseToken(TokenType.IntegerLiteral);
        Integer value = ((IntegerLiteralToken)token).value;

        LiteralExpressionNode expression = new LiteralExpressionNode(value);
        visitor.accept(expression);
        return expression;
    }
    public LiteralExpressionNode(Integer value) {
        this.value = value;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Literal;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {});
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