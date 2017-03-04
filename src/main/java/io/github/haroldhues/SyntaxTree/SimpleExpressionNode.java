package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;



public class SimpleExpressionNode extends SyntaxTreeNode {
	public AdditiveNode left;
    public Token compare; // <=, <, >=, >, ==, !=
    public AdditiveNode right;

    public SimpleExpressionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
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
        builder.append(left);
        if (compare != null) {
            builder.append(compare);
            builder.append(right);
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.left)
			.property(o -> o.compare)
			.property(o -> o.right)
			.result(this, other);
    }
}