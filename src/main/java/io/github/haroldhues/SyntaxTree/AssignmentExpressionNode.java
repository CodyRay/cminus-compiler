package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Tokens.*;

public class AssignmentExpressionNode extends ExpressionNode {
    public VariableExpressionNode variable;
    public ExpressionNode expression;

    public AssignmentExpressionNode(VariableExpressionNode variable, ExpressionNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Assignment;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(variable, visitor);
            SyntaxTreeNode.visit(expression, visitor);
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(variable);
        builder.append(new Token(TokenType.Assign));
        builder.append(expression);
        return builder.toString();
    }

    public boolean equals(Object other) {
        return equalsBuilder(this)
            .property(o -> o.variable)
            .property(o -> o.expression)
            .result(this, other);
    }
}