package io.github.haroldhues.SyntaxTree;
import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;



public class IterationStatementNode extends StatementNode {
    public ExpressionNode condition;
    public StatementNode block;

    public static IterationStatementNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        parser.parseToken(TokenType.While);
        parser.parseToken(TokenType.LeftParenthesis);
        ExpressionNode condition = ExpressionNode.parse(parser);
        parser.parseToken(TokenType.RightParenthesis);
        StatementNode block = StatementNode.parse(parser);

        return new IterationStatementNode(location, condition, block);
    }

    public IterationStatementNode(Location location, ExpressionNode condition, StatementNode block) {
    	super(location);
        this.condition = condition;
        this.block = block;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Iteration;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.While));
        builder.append(new Token(TokenType.LeftParenthesis));
        builder.append(condition);
        builder.append(new Token(TokenType.RightParenthesis));
        builder.append(block);
        return builder.toString();
    }

    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.condition)
			.property(o -> o.block)
			.result(this, other);
    }
}