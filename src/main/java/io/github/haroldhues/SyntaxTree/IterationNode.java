package io.github.haroldhues.SyntaxTree;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;



public class IterationNode extends SyntaxTreeNode {
    public ExpressionNode condition;
    public StatementNode block;

    public IterationNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.While);
        parser.parseToken(TokenType.LeftParenthesis);
        condition = new ExpressionNode(parser, visitor);
        parser.parseToken(TokenType.RightParenthesis);
        block = new StatementNode(parser, visitor);
        visitor.accept(this);
    }

    public IterationNode(ExpressionNode condition, StatementNode block) {
        this.condition = condition;
        this.block = block;
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