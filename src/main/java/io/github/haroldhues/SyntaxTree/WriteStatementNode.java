package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;


public class WriteStatementNode extends StatementNode {
    public ExpressionNode expression;
    public static WriteStatementNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        parser.parseToken(TokenType.Write);
        ExpressionNode expression = ExpressionNode.parse(parser);
        parser.parseToken(TokenType.Semicolon);

        return new WriteStatementNode(location, expression);
    }

    public WriteStatementNode(Location location, ExpressionNode expression) {
    	super(location);
        this.expression = expression;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Write;
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