package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;


public class WriteStatementNode extends StatementNode {
    public ExpressionNode expression;
    public static WriteStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.Write);
        ExpressionNode expression = ExpressionNode.parse(parser, visitor);
        parser.parseToken(TokenType.Semicolon);

        WriteStatementNode statement = new WriteStatementNode(expression);
        visitor.accept(statement);
        return statement;
    }

    public WriteStatementNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Write;
    }

	public boolean allPathsReturn() {
		return false; // write != return
	}

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(expression, visitor);
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.Write));
        builder.append(expression.toString().trim());
        builder.append(new Token(TokenType.Semicolon));
        return builder.toString();
    }

    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.expression)
			.result(this, other);
    }
}