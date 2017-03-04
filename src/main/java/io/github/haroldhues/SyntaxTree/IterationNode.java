package io.github.haroldhues.SyntaxTree;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
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
        builder.append("while (");
        builder.append(condition);
        builder.append(") ");
        builder.append(block);
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof IterationNode)) {
            return false;
        }
         
        IterationNode that = (IterationNode) other;
 
        return this.condition.equals(that.condition) && this.block.equals(that.block);
    }
}