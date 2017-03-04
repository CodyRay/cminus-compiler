package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.Token;
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

    public ParamNode(TypeSpecifierNode typeSpecifier, String identifier, boolean isArray) {
        this.typeSpecifier = typeSpecifier;
        this.identifier = identifier;
        this.isArray = isArray;
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