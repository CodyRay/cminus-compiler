package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class ReturnStatementNode extends SyntaxTreeNode {
    public ExpressionNode expression;
    public ReturnStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
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
        builder.append("return ");
        builder.append(expression.toString());
        builder.append(";");
        return builder.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ReturnStatementNode)) {
            return false;
        }
         
        ReturnStatementNode that = (ReturnStatementNode) other;
 
        return this.expression.equals(that.expression);
    }
}