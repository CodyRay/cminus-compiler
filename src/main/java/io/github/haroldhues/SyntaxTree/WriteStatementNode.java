package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;


public class WriteStatementNode extends SyntaxTreeNode {
    public ExpressionNode expression;
    public WriteStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.Write);
        expression = new ExpressionNode(parser, visitor);
        parser.parseToken(TokenType.Semicolon);

        visitor.accept(this);
    }

    public WriteStatementNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("write ");
        builder.append(expression.toString());
        builder.append(";");
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof WriteStatementNode)) {
            return false;
        }
         
        WriteStatementNode that = (WriteStatementNode) other;
 
        return this.expression.equals(that.expression);
    }
}