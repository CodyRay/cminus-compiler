package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;


public class WriteStatementNode extends SyntaxTreeNode {
    public ExpressionNode expression;
    public WriteStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.Write);
        expression = new ExpressionNode(parser, visitor);
        parser.parseToken(TokenType.Semicolon);

        visitor.accept(this);
    }

    public WriteStatementNode(ExpressionNode expression) {
        this.expression = expression;
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