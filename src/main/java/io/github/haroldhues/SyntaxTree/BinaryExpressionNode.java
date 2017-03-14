package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.Tokens.*;

public class BinaryExpressionNode extends ExpressionNode {
    public ExpressionNode left;
    public Token operation;
    public ExpressionNode right;

    public BinaryExpressionNode(ExpressionNode left, Token operation, ExpressionNode right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.BinaryOperator;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(left.toString());
        builder.append(operation.toString());
        builder.append(right.toString());
        return builder.toString();
    }

    public boolean equals(Object other) {
        return equalsBuilder(this)
            .property(o -> o.left)
            .property(o -> o.operation)
            .property(o -> o.right)
            .result(this, other);
    }
}