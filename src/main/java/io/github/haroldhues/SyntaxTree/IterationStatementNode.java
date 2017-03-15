package io.github.haroldhues.SyntaxTree;
import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;



public class IterationStatementNode extends StatementNode {
    public ExpressionNode condition;
    public StatementNode block;

    public static IterationStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.While);
        parser.parseToken(TokenType.LeftParenthesis);
        ExpressionNode condition = ExpressionNode.parse(parser, visitor);
        parser.parseToken(TokenType.RightParenthesis);
        StatementNode block = StatementNode.parse(parser, visitor);

        IterationStatementNode statement = new IterationStatementNode(condition, block);
        visitor.accept(statement);
        return statement;
    }

    public IterationStatementNode(ExpressionNode condition, StatementNode block) {
        this.condition = condition;
        this.block = block;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Iteration;
    }

	public boolean allPathsReturn() {
		return false; // if the condition is never true it cannot return
	}

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(condition, visitor);
            SyntaxTreeNode.visit(block, visitor);
        });
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