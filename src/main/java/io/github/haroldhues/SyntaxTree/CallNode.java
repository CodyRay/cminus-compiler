package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class CallNode extends SyntaxTreeNode {
    public String identifier;
    public List<ExpressionNode> arguments;

    public CallNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        arguments = parseCallArgs(parser, visitor);
        visitor.accept(this);
    }

    public CallNode(String identifier, List<ExpressionNode> arguments, Consumer<SyntaxTreeNode> visitor) {
        this.identifier = identifier;
        this.arguments = arguments;
        visitor.accept(this);
    }

    public static List<ExpressionNode> parseCallArgs(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();
        parser.parseToken(TokenType.LeftParenthesis);
        if(!parser.parseTokenIf(TokenType.RightParenthesis)) {
            do {
                args.add(new ExpressionNode(parser, visitor));
            } while(parser.parseTokenIf(TokenType.Comma));
            parser.parseToken(TokenType.RightParenthesis);
        }
        return args;
    }
}