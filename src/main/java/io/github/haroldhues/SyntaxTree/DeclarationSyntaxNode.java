package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.IntegerLiteralToken;
import io.github.haroldhues.Tokens.TokenType;





public class DeclarationSyntaxNode extends SyntaxTreeNode {
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

    public DeclarationSyntaxNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        typeSpecifier = new TypeSpecifierNode(parser, visitor);
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
            functionParams = new ParamsNode(parser, visitor);
            parser.parseToken(TokenType.RightParenthesis);
            functionBody = new CompoundStatementNode(parser, visitor);

        } else {
            parser.throwExpected(TokenType.Semicolon, TokenType.LeftBracket, TokenType.LeftParenthesis);
        }
        visitor.accept(this);
    }
}