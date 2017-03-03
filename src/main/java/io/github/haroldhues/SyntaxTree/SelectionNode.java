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
}