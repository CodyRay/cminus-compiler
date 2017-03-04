package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;



public class SimpleExpressionNode extends SyntaxTreeNode {
	public AdditiveNode left;
    public Token compare; // <=, <, >=, >, ==, !=
    public AdditiveNode right;

    public SimpleExpressionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        left = new AdditiveNode(parser, visitor);
        if(parser.currentToken().isCompareOperator()) {
            compare = parser.currentToken();
            // Already been looked at for the comparison
            parser.moveNextToken();
            right = new AdditiveNode(parser, visitor);
        }
        visitor.accept(this);
    }

    public SimpleExpressionNode(AdditiveNode expression) {
        this.left = expression;
    }

    public SimpleExpressionNode(AdditiveNode left, Token compare, AdditiveNode right) {
        this.left = left;
        this.compare = compare;
        this.right = right;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(left.toString());
        if (compare != null) {
            builder.append(' ');
            builder.append(compare.toString());
            builder.append(right.toString());
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof SimpleExpressionNode)) {
            return false;
        }
         
        SimpleExpressionNode that = (SimpleExpressionNode) other;
 
        return this.left.equals(that.left) && 
            this.right.equals(that.right) && 
            this.compare.equals(that.compare);
    }
}