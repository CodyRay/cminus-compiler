package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class ExpressionStatementNode extends SyntaxTreeNode {
	public ExpressionNode expression;

	public ExpressionStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
		if (!parser.parseTokenIf(TokenType.Semicolon)) {
			expression = new ExpressionNode(parser, visitor);
			parser.parseToken(TokenType.Semicolon);
		}
		visitor.accept(this);
	}

	public ExpressionStatementNode(ExpressionNode expression) {
		this.expression = expression;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (expression != null) {
			builder.append(expression.toString().trim());
		}
		builder.append(new Token(TokenType.Semicolon));
		return builder.toString();
	}

	public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.expression)
			.result(this, other);
	}
}