package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.Location;
import io.github.haroldhues.Tokens.*;

public class AssignmentExpressionNode extends ExpressionNode {
	public VariableExpressionNode variable;
	public ExpressionNode expression;

	public AssignmentExpressionNode(Location location, VariableExpressionNode variable, ExpressionNode expression) {
		super(location);
		this.variable = variable;
		this.expression = expression;
	}

	public ExpressionNode.Type expressionType() {
		return ExpressionNode.Type.Assignment;
	}

	public String toAstString() {
		return buildAstString("AssignmentExpressionNode", location.toAstString(), variable.toAstString(), expression.toAstString());
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(variable);
		builder.append(new Token(TokenType.Assign));
		builder.append(expression);
		return builder.toString();
	}

	public boolean equals(Object other) {
		return equalsBuilder(this).property(o -> o.variable).property(o -> o.expression).result(this, other);
	}
}