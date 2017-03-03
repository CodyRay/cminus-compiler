package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;





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
}