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
}