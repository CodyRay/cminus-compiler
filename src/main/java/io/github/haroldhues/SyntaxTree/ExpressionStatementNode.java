package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class ExpressionStatementNode extends StatementNode {
	public ExpressionNode expression;

	public static ExpressionStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
		ExpressionNode expression = null;
		if (!parser.parseTokenIf(TokenType.Semicolon)) {
			expression = ExpressionNode.parse(parser, visitor);
			parser.parseToken(TokenType.Semicolon);
		}
		
		ExpressionStatementNode statement = new ExpressionStatementNode(expression);
		visitor.accept(statement);
		return statement;
	}

	public ExpressionStatementNode(ExpressionNode expression) {
		this.expression = expression;
	}

    public StatementNode.Type statementType() {
        return StatementNode.Type.Expression;
    }

	public boolean allPathsReturn() {
		return false; // There can be no returns from an expression
	}

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(expression, visitor);
        });
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