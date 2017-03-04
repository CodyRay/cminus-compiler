package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class ReturnStatementNode extends SyntaxTreeNode {
    public ExpressionNode expression;
    public ReturnStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.Return);
        if(!parser.parseTokenIf(TokenType.Semicolon)) {
            expression = new ExpressionNode(parser, visitor);
            parser.parseToken(TokenType.Semicolon);
        }
        visitor.accept(this);
    }

    public ReturnStatementNode(ExpressionNode expressionNode) {
        this.expression = expressionNode;
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