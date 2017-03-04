package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class SelectionNode extends SyntaxTreeNode {
    public ExpressionNode condition;
    public StatementNode ifBlock;
    public StatementNode elseBlock;
    public SelectionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.If);
        parser.parseToken(TokenType.LeftParenthesis);
        condition = new ExpressionNode(parser, visitor);
        parser.parseToken(TokenType.RightParenthesis);
        ifBlock = new StatementNode(parser, visitor);
        if(parser.parseTokenIf(TokenType.Else)) {
            elseBlock = new StatementNode(parser, visitor);
        }
        visitor.accept(this);
    }

    public SelectionNode(ExpressionNode condition, StatementNode ifBlock, StatementNode elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.If));
        builder.append(new Token(TokenType.LeftParenthesis));
        builder.append(condition.toString());
        builder.append(new Token(TokenType.RightParenthesis));
        builder.append(ifBlock.toString());
        if(elseBlock != null) {
            builder.append(new Token(TokenType.Else));
            builder.append(elseBlock.toString());
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.condition)
			.property(o -> o.ifBlock)
			.property(o -> o.elseBlock)
			.result(this, other);
    }
}