package io.github.haroldhues;

import io.github.haroldhues.SyntaxTree.*;
import io.github.haroldhues.Tokens.*;

public class Parser {
    private Enumerable<Token> source;
    private Token currentItem;

    public Parser(Enumerable<Token> source) throws CompileErrorException {
        this.source = source;
        // Read first token into lookahead variable
        moveNextToken();
    }

    public Token currentToken() throws CompileErrorException {
        if (currentItem == null) {
            // Theoretically, this should never happen because there is an EOF token that should happen first
            throw new CompileErrorException("Unexpected end of file");
        }
        return currentItem;
    }

    public Location currentLocation() throws CompileErrorException {
        return currentToken().getLocation();
    }

    public boolean currentIs(TokenType type) throws CompileErrorException {
        return currentToken().type == type;
    }

    public void moveNextToken() throws CompileErrorException {
        currentItem = source.next();
    }

    public boolean parseTokenIf(TokenType type) throws CompileErrorException {
        if(!currentIs(type)) {
            return false;
        }
        moveNextToken();
        return true;
    }

    public Token parseToken(TokenType type) throws CompileErrorException {
        if(!currentIs(type)) {
            throwExpected(type);
        }
        Token token = currentToken();
        moveNextToken();
        return token;
    }

    public void throwExpected(TokenType ...expected) throws CompileErrorException {
        StringBuilder builder = new StringBuilder();
        builder.append("Expected ");
        for(int x = 0; x < expected.length; x++) {
            TokenType type = expected[x];
            if (type == TokenType.Identifier) {
                builder.append("an identifier");
            } else if (type == TokenType.IntegerLiteral) {
                builder.append("an integer");
            } else {
                builder.append("'" + new Token(type).toString().trim() + "'");
            }

            if (x < expected.length - 2) {
                builder.append(", ");
            } else if (x == expected.length - 2) {
                if (expected.length == 2) {
                    builder.append(" or ");
                } else {
                    builder.append(", or ");
                }
            }
        }

        builder.append(" but instead found ");
        builder.append(currentToken().toString().trim());

        throw new CompileErrorException(builder.toString(), currentToken().getLocation());
    }

    public RootNode parse() throws CompileErrorException {
        return RootNode.parse(this);
    }
}