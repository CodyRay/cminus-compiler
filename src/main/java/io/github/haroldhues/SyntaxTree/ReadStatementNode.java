package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class ReadStatementNode extends StatementNode {
    public VariableExpressionNode reference;
    
    public static ReadStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.Read);
        VariableExpressionNode reference;
        if(parser.parseTokenIf(TokenType.LeftParenthesis)) {
            reference = VariableExpressionNode.parse(parser, visitor);
            parser.parseToken(TokenType.RightParenthesis);
        } else {
            reference = VariableExpressionNode.parse(parser, visitor);
        }
        parser.parseToken(TokenType.Semicolon);

        ReadStatementNode statement = new ReadStatementNode(reference);
        visitor.accept(statement);
        return statement;
    }

    public ReadStatementNode(VariableExpressionNode reference) {
        this.reference = reference;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Read;
    }

	public boolean allPathsReturn() {
		return false; // read != return
	}

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(reference, visitor);
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.Read));
        builder.append(reference.toString().trim());
        builder.append(new Token(TokenType.Semicolon));
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.reference)
			.result(this, other);
    }
}