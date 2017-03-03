package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.TokenType;





public class VariableNode extends SyntaxTreeNode {
    public String identifier;
    public ExpressionNode arrayExpression;
    
    public VariableNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        IdentifierToken idToken = (IdentifierToken)parser.parseToken(TokenType.Identifier);
        identifier = idToken.identifier;
        arrayExpression = parseArrayNotation(parser, visitor);

        visitor.accept(this);
    }

    public VariableNode(String identifier, ExpressionNode arrayExpression, Consumer<SyntaxTreeNode> visitor) {
        this.identifier = identifier;
        this.arrayExpression = arrayExpression;

        visitor.accept(this);
    }

    public VariableNode(String identifier, Consumer<SyntaxTreeNode> visitor) {
        this.identifier = identifier;

        visitor.accept(this);
    }

    public static ExpressionNode parseArrayNotation(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        ExpressionNode returnValue = null;
        if(parser.parseTokenIf(TokenType.LeftBracket)) {
            returnValue = new ExpressionNode(parser, visitor);
            parser.parseToken(TokenType.RightBracket);
        }
        return returnValue;
    }
}