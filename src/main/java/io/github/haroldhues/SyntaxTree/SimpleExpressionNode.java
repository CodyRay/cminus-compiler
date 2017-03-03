package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;



public class SimpleExpressionNode extends SyntaxTreeNode {
	public AdditiveNode left;
    public Comparison compare;
    public AdditiveNode right;

    public enum Comparison {
        LessThanOrEqual,
        LessThan,
        GreaterThanOrEqual,
        GreaterThan,
        Equal,
        NotEqual,
        None, // No Right Node present
    }

    public SimpleExpressionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        left = new AdditiveNode(parser, visitor);
        compare = getComparisonFromToken(parser.currentToken());
        if(compare != Comparison.None) {
            parser.moveNextToken(); // Already been looked at for the comparison
            right = new AdditiveNode(parser, visitor);
        }
        visitor.accept(this);
    }

    public SimpleExpressionNode(AdditiveNode expression, Consumer<SyntaxTreeNode> visitor) {
        compare = Comparison.None;
        this.left = expression;
        visitor.accept(this);
    }

    public SimpleExpressionNode(AdditiveNode left, Comparison compare, AdditiveNode right, Consumer<SyntaxTreeNode> visitor) {
        this.left = left;
        this.compare = compare;
        this.right = right;
        visitor.accept(this);
    }

    public static Comparison getComparisonFromToken(Token token) throws Exception {
        switch(token.type) {
            case LessThanOrEqual:
                return Comparison.LessThanOrEqual;
            case LessThan:
                return Comparison.LessThan;
            case GreaterThanOrEqual:
                return Comparison.GreaterThanOrEqual;
            case GreaterThan:
                return Comparison.GreaterThan;
            case Equal:
                return Comparison.Equal;
            case NotEqual:
                return Comparison.NotEqual;
            default:
                return Comparison.None;
        }
    }
}