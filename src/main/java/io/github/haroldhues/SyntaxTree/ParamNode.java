package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.TokenType;





public class ParamNode extends SyntaxTreeNode {

    public TypeSpecifierNode typeSpecifier;
    public String identifier;
    public boolean isArray = false;
    public ParamNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        typeSpecifier = new TypeSpecifierNode(parser, visitor);
        identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            isArray = true;
            parser.parseToken(TokenType.RightBracket);
        }
        visitor.accept(this);
    }
}