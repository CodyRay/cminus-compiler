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

    public DeclarationSyntaxNode(TypeSpecifierNode typeSpecifier, String identifier, ParamsNode functionParams, CompoundStatementNode functionBody) {
        type = Type.Function;
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
        this.functionParams = functionParams;
        this.functionBody = functionBody;
    }

    public DeclarationSyntaxNode(TypeSpecifierNode typeSpecifier, String identifier) {
        type = Type.Variable;
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
    }

    public DeclarationSyntaxNode(TypeSpecifierNode typeSpecifier, String identifier, int arraySize) {
        type = Type.Variable;
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
        this.arraySize = arraySize;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(typeSpecifier);
        builder.append(' ');
        builder.append(identifier);
        if(type == Type.ArrayVariable) {
            builder.append('[');
            builder.append(arraySize);
            builder.append(']');
            builder.append(';');
        } else if (type == Type.Variable) {
            builder.append(';');
        } else {
            builder.append('(');
            builder.append(functionParams);
            builder.append(") ");
            builder.append(functionBody);
        }
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof DeclarationSyntaxNode)) {
            return false;
        }
         
        DeclarationSyntaxNode that = (DeclarationSyntaxNode) other;
 
        return this.typeSpecifier.equals(that.typeSpecifier) &&
            this.identifier.equals(that.identifier) &&
            this.type.equals(that.type) &&
            this.arraySize == that.arraySize &&
            this.functionParams.equals(that.functionParams) &&
            this.functionBody.equals(that.functionBody);
    }
}