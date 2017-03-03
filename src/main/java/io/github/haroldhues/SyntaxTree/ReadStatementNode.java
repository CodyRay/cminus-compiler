package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;

public class ReadStatementNode extends SyntaxTreeNode {
    public VariableNode reference;
    public ReadStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.Read);
        if(parser.parseTokenIf(TokenType.LeftParenthesis)) {
            reference = new VariableNode(parser, visitor);
            parser.parseToken(TokenType.RightParenthesis);
        }
        visitor.accept(this);
    }
}