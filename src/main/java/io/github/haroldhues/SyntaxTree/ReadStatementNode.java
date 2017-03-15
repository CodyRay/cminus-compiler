package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class ReadStatementNode extends StatementNode {
    public VariableExpressionNode reference;
    
    public static ReadStatementNode parse(Parser parser) throws CompileErrorException {
        parser.parseToken(TokenType.Read);
        VariableExpressionNode reference;
        if(parser.parseTokenIf(TokenType.LeftParenthesis)) {
            reference = VariableExpressionNode.parse(parser);
            parser.parseToken(TokenType.RightParenthesis);
        } else {
            reference = VariableExpressionNode.parse(parser);
        }
        parser.parseToken(TokenType.Semicolon);

        ReadStatementNode statement = new ReadStatementNode(parser.currentLocation(), reference);
        return statement;
    }

    public ReadStatementNode(Location location, VariableExpressionNode reference) {
    	super(location);
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