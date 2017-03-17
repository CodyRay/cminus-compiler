package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class SelectionStatementNode extends StatementNode {
    public ExpressionNode condition;
    public StatementNode ifBlock;
    public StatementNode elseBlock;

    public static SelectionStatementNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        parser.parseToken(TokenType.If);
        parser.parseToken(TokenType.LeftParenthesis);
        ExpressionNode condition = ExpressionNode.parse(parser);
        parser.parseToken(TokenType.RightParenthesis);
        StatementNode ifBlock = StatementNode.parse(parser);
        
        StatementNode elseBlock = null;
        if(parser.parseTokenIf(TokenType.Else)) {
            elseBlock = StatementNode.parse(parser);
        }

        return new SelectionStatementNode(location, condition, ifBlock, elseBlock);
    }

    public SelectionStatementNode(Location location, ExpressionNode condition, StatementNode ifBlock, StatementNode elseBlock) {
    	super(location);
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Selection;
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