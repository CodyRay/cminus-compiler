package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class ParameterDeclarationNode extends SyntaxTreeNode {

    public TypeSpecifierNode typeSpecifier;
    public String identifier;
    public boolean isArray = false;

    public static ParameterDeclarationNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        TypeSpecifierNode typeSpecifier = TypeSpecifierNode.parse(parser);
        String identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        boolean isArray = false;

        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            isArray = true;
            parser.parseToken(TokenType.RightBracket);
        }

        return new ParameterDeclarationNode(location, typeSpecifier, identifier, isArray);
    }

    public ParameterDeclarationNode(Location location, TypeSpecifierNode typeSpecifier, String identifier, boolean isArray) {
    	super(location);
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
        this.isArray = isArray;
    }

    public String toAstString() {
        return buildAstString("ParameterDeclarationNode", location.toAstString(), typeSpecifier.toAstString(), "\"" + identifier + "\"", isArray ? "true" : "false");
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