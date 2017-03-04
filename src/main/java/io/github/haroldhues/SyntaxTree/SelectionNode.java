package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;





public class SelectionNode extends SyntaxTreeNode {
    public ExpressionNode condition;
    public StatementNode ifBlock;
    public StatementNode elseBlock;
    public SelectionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
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
        builder.append("if (");
        builder.append(condition.toString());
        builder.append(") ");
        builder.append(condition.toString());
        if(elseBlock != null) {
            builder.append(" else ");
            builder.append(elseBlock.toString());
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof SelectionNode)) {
            return false;
        }
         
        SelectionNode that = (SelectionNode) other;
 
        return this.condition.equals(that.condition) && 
            this.ifBlock.equals(that.ifBlock) && 
            this.elseBlock.equals(that.elseBlock);
    }
}