package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;


public class WriteStatementNode extends SyntaxTreeNode {
    public ExpressionNode expression;
    public WriteStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.Write);
        expression = new ExpressionNode(parser, visitor);

        visitor.accept(this);
    }
}