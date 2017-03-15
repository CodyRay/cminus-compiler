package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.IntegerLiteralToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public class DeclarationNode extends SyntaxTreeNode {
	public enum Type {
		Variable, 
		ArrayVariable, 
		Function,
	}

	public TypeSpecifierNode typeSpecifier;
	public String identifier;
	public Type type;
	public int arraySize;
    public List<ParameterDeclarationNode> functionParameters = new ArrayList<ParameterDeclarationNode>();
	public CompoundStatementNode functionBody;

	public static DeclarationNode parse(Parser parser) throws CompileErrorException {
		TypeSpecifierNode typeSpecifier = TypeSpecifierNode.parse(parser);
		String identifier;
		if (!parser.currentIs(TokenType.Identifier)) {
			parser.throwExpected(TokenType.Identifier);
		}
		identifier = ((IdentifierToken) parser.currentToken()).identifier;
		parser.moveNextToken();
		
		DeclarationNode declaration;
		if (parser.parseTokenIf(TokenType.Semicolon)) {
			declaration = new DeclarationNode(parser.currentLocation(), typeSpecifier, identifier);
		} else if (parser.parseTokenIf(TokenType.LeftBracket)) {
			int arraySize = ((IntegerLiteralToken) parser.parseToken(TokenType.IntegerLiteral)).value;
			parser.parseToken(TokenType.RightBracket);
			parser.parseToken(TokenType.Semicolon);
			declaration = new DeclarationNode(parser.currentLocation(), typeSpecifier, identifier, arraySize);
		} else if (parser.parseTokenIf(TokenType.LeftParenthesis)) {
			List<ParameterDeclarationNode> functionParameters = parseFunctionParameters(parser);
			parser.parseToken(TokenType.RightParenthesis);
			CompoundStatementNode functionBody = CompoundStatementNode.parse(parser);
			declaration = new DeclarationNode(parser.currentLocation(), typeSpecifier, identifier, functionParameters, functionBody);
		} else {
			parser.throwExpected(TokenType.Semicolon, TokenType.LeftBracket, TokenType.LeftParenthesis);
			declaration = null; // Unreachable Code
		}

		return declaration;
	}

	public static List<ParameterDeclarationNode> parseFunctionParameters(Parser parser) throws CompileErrorException {
		List<ParameterDeclarationNode> parameters = new ArrayList<ParameterDeclarationNode>();
		if(!parser.parseTokenIf(TokenType.Void)) {
			do {
				parameters.add(ParameterDeclarationNode.parse(parser));
			}
			while(parser.parseTokenIf(TokenType.Comma));
		}
		return parameters;
	}

	public DeclarationNode(Location location, TypeSpecifierNode typeSpecifier, String identifier,
			List<ParameterDeclarationNode> functionParameters, CompoundStatementNode functionBody) {
		super(location);
		type = Type.Function;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
		this.functionParameters = functionParameters;
		this.functionBody = functionBody;
	}

	public DeclarationNode(Location location, TypeSpecifierNode typeSpecifier, String identifier) {
		super(location);
		type = Type.Variable;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
	}

	public DeclarationNode(Location location, TypeSpecifierNode typeSpecifier, String identifier, int arraySize) {
		super(location);
		type = Type.ArrayVariable;
		this.typeSpecifier = typeSpecifier;
		this.identifier = identifier;
		this.arraySize = arraySize;
	}

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            SyntaxTreeNode.visit(typeSpecifier, visitor);
            for(ParameterDeclarationNode parameter: functionParameters) {
                SyntaxTreeNode.visit(parameter, visitor);
            }
            SyntaxTreeNode.visit(functionBody, visitor);
        });
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