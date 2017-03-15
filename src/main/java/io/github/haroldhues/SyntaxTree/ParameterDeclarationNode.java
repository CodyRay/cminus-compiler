package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class ParameterDeclarationNode extends SyntaxTreeNode {

    public TypeSpecifierNode typeSpecifier;
    public String identifier;
    public boolean isArray = false;

    public static ParameterDeclarationNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        TypeSpecifierNode typeSpecifier = TypeSpecifierNode.parse(parser, visitor);
        String identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        boolean isArray = false;

        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            isArray = true;
            parser.parseToken(TokenType.RightBracket);
        }

        ParameterDeclarationNode declaration = new ParameterDeclarationNode(typeSpecifier, identifier, isArray);
        visitor.accept(declaration);
        return declaration;
    }

    public ParameterDeclarationNode(TypeSpecifierNode typeSpecifier, String identifier, boolean isArray) {
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
        this.isArray = isArray;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(typeSpecifier, visitor);
        });
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(typeSpecifier);
        builder.append(identifier.trim());
        if(isArray) {
            builder.append(new Token(TokenType.LeftBracket).toString().trim());
            builder.append(new Token(TokenType.RightBracket).toString().trim());
        }
        return builder.toString();
    }

    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.typeSpecifier)
			.property(o -> o.identifier)
			.property(o -> o.isArray)
			.result(this, other);
    }
}