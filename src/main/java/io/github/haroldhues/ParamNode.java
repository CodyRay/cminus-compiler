package io.github.haroldhues;

public class ParamNode extends SyntaxTree {

    public TypeSpecifierNode typeSpecifier;
    public String identifier;
    public boolean isArray = false;
    public ParamNode(Parser parser) throws Exception {
        typeSpecifier = new TypeSpecifierNode(parser);
        identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            isArray = true;
            parser.parseToken(TokenType.RightBracket);
        }
        parser.visit(this);
    }
}