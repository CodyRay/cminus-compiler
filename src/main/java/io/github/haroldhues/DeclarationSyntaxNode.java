package io.github.haroldhues;

public class DeclarationSyntaxNode extends SyntaxTree {
    public enum Type {
        Variable,
        ArrayVariable,
        Function,
    }

    public TypeSpecifierNode typeSpecifier;
    public String identifier;
    public Type type;
    public int arraySize;
    public ParamsNode functionParams;
    public CompoundStatementNode functionBody;

    public DeclarationSyntaxNode(Parser parser) throws Exception {
        typeSpecifier = new TypeSpecifierNode(parser);
        if(parser.currentIs(TokenType.Identifier)) {
            identifier = ((IdentifierToken)parser.currentToken()).identifier;
        } else {
            parser.throwExpected(TokenType.Identifier);
        }

        if(parser.parseTokenIf(TokenType.Semicolon)) {
            type = Type.Variable;
        } else if(parser.parseTokenIf(TokenType.LeftBracket)) {
            type = Type.ArrayVariable;
            arraySize = ((IntegerLiteralToken)parser.parseToken(TokenType.IntegerLiteral)).value;
            parser.parseToken(TokenType.RightBracket);
            parser.parseToken(TokenType.Semicolon);
        } else if(parser.parseTokenIf(TokenType.LeftParenthesis)) {
            type = Type.Function;
            functionParams = new ParamsNode(parser);
            parser.parseToken(TokenType.RightParenthesis);
            functionBody = new CompoundStatementNode(parser);

        } else {
            parser.throwExpected(TokenType.Semicolon, TokenType.LeftBracket, TokenType.LeftParenthesis);
        }
        parser.visit(this);
    }
}