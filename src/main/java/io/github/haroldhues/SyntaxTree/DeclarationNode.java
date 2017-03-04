package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.IntegerLiteralToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class DeclarationNode extends SyntaxTreeNode {
	public enum Type {
		Variable, ArrayVariable, Function,
	}

	public TypeSpecifierNode typeSpecifier;
	public String identifier;
	public Type type;
	public int arraySize;
    public List<ParameterDeclarationNode> functionParameters = new ArrayList<ParameterDeclarationNode>();
	public CompoundStatementNode functionBody;

	public DeclarationNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
		typeSpecifier = new TypeSpecifierNode(parser, visitor);
		if (parser.currentIs(TokenType.Identifier)) {
			identifier = ((IdentifierToken) parser.currentToken()).identifier;
			parser.moveNextToken();
		} else {
			parser.throwExpected(TokenType.Identifier);
		}

		if (parser.parseTokenIf(TokenType.Semicolon)) {
			type = Type.Variable;
		} else if (parser.parseTokenIf(TokenType.LeftBracket)) {
			type = Type.ArrayVariable;
			arraySize = ((IntegerLiteralToken) parser.parseToken(TokenType.IntegerLiteral)).value;
			parser.parseToken(TokenType.RightBracket);
			parser.parseToken(TokenType.Semicolon);
		} else if (parser.parseTokenIf(TokenType.LeftParenthesis)) {
			type = Type.Function;
			functionParameters = parseFunctionParameters(parser, visitor);
			parser.parseToken(TokenType.RightParenthesis);
			functionBody = new CompoundStatementNode(parser, visitor);

		} else {
			parser.throwExpected(TokenType.Semicolon, TokenType.LeftBracket, TokenType.LeftParenthesis);
		}
		visitor.accept(this);
	}

	public static List<ParameterDeclarationNode> parseFunctionParameters(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
		List<ParameterDeclarationNode> parameters = new ArrayList<ParameterDeclarationNode>();
		if(!parser.parseTokenIf(TokenType.Void)) {
			do {
				parameters.add(new ParameterDeclarationNode(parser, visitor));
			}
			while(parser.parseTokenIf(TokenType.Comma));
		}
		return parameters;
	}

	public DeclarationNode(TypeSpecifierNode typeSpecifier, String identifier, List<ParameterDeclarationNode> functionParameters,
			CompoundStatementNode functionBody) {
		type = Type.Function;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
		this.functionParameters = functionParameters;
		this.functionBody = functionBody;
	}

	public DeclarationNode(TypeSpecifierNode typeSpecifier, String identifier) {
		type = Type.Variable;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
	}

	public DeclarationNode(TypeSpecifierNode typeSpecifier, String identifier, int arraySize) {
		type = Type.Variable;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
		this.arraySize = arraySize;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(typeSpecifier);
		builder.append(identifier);
		if (type == Type.ArrayVariable) {
			builder.append(new Token(TokenType.LeftBracket));
			builder.append(new IntegerLiteralToken(arraySize));
			builder.append(new Token(TokenType.RightBracket).toString().trim());
			builder.append(new Token(TokenType.Semicolon));
		} else if (type == Type.Variable) {
			builder.append(new Token(TokenType.Semicolon));
		} else {
			builder.append(new Token(TokenType.LeftParenthesis));
			if(functionParameters.size() > 0) {
				String delimiter = "";
				for(ParameterDeclarationNode param : functionParameters) {
					builder.append(delimiter);
					builder.append(param);
					delimiter = new Token(TokenType.Comma).toString();
				}
			} else {
				builder.append(new Token(TokenType.Void));
			}
			builder.append(new Token(TokenType.RightParenthesis));
			builder.append(functionBody);
		}
		return builder.toString();
	}

	public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.typeSpecifier)
			.property(o -> o.identifier)
			.property(o -> o.type)
			.property(o -> o.arraySize)
			.property(o -> o.functionParameters)
			.property(o -> o.functionBody)
			.result(this, other);
	}
}