package io.github.haroldhues;

import java.util.function.Consumer;

import io.github.haroldhues.SyntaxTree.*;
import io.github.haroldhues.Tokens.*;

public class Parser {
    private Enumerable<Token> source;
    private Enumerable<Token>.Item currentItem;

    public Parser(Enumerable<Token> source) {
        this.source = source;
    }

    public Token currentToken() throws Exception {
        if(currentItem == null) {
            currentItem = source.next();
        }

        if (currentItem.getStatus() == Enumerable.Status.Value) {
            return currentItem.getValue();
        } else if (currentItem.getStatus() == Enumerable.Status.End) {
            throw new Exception("Unexpected End of Input");
        } else {
            throw new Exception(currentItem.getError().message);
        }
    }

    public boolean currentIs(TokenType type) throws Exception {
        return currentToken().type == type;
    }

    public void moveNextToken() {
        currentItem = source.next();
    }

    public boolean parseTokenIf(TokenType type) throws Exception {
        if(!currentIs(type)) {
            return false;
        }
        moveNextToken();
        return true;
    }

    public Token parseToken(TokenType type) throws Exception {
        if(!currentIs(type)) {
            throwExpected(type);
        }
        Token token = currentToken();
        moveNextToken();
        return token;
    }

    public void throwExpected(TokenType ...expected) throws Exception {
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

        throw new Exception(builder.toString());
    }

    public ProgramSyntaxNode parse(Consumer<SyntaxTreeNode> visitor) throws Exception {
        return new ProgramSyntaxNode(this, visitor);
    }
}