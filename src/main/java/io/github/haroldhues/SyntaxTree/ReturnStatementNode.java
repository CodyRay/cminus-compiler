package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class ReturnStatementNode extends StatementNode {
    public ExpressionNode expression;

    public static ReturnStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.Return);
        ExpressionNode expression = null;
        if(!parser.parseTokenIf(TokenType.Semicolon)) {
            expression = ExpressionNode.parse(parser, visitor);
            parser.parseToken(TokenType.Semicolon);
        }

        ReturnStatementNode statement = new ReturnStatementNode(expression);
        visitor.accept(statement);
        return statement;
    }

    public ReturnStatementNode(ExpressionNode expressionNode) {
        this.expression = expressionNode;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Return;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(expression != null) {
        	builder.append(new Token(TokenType.Return));
        	builder.append(expression.toString().trim());        	
        } else {
        	builder.append(new Token(TokenType.Return).toString().trim());
        }
        builder.append(new Token(TokenType.Semicolon));
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.expression)
			.result(this, other);
    }
}