package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.IntegerLiteralToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class FactorNode extends SyntaxTreeNode {
    public enum Type {
        Expression,
        Variable,
        Call,
        IntegerValue,
    }

    public Type type;
    public int integerValue;
    public CallNode call;
    public VariableNode variable;
    public ExpressionNode expression;

    public FactorNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        if(parser.currentIs(TokenType.IntegerLiteral)) {
            type = Type.IntegerValue;
            Token token = parser.parseToken(TokenType.IntegerLiteral);
            integerValue = ((IntegerLiteralToken)token).value;
        } else if (parser.parseTokenIf(TokenType.LeftParenthesis)) {
            type = Type.Expression;
            expression = new ExpressionNode(parser, visitor);
            parser.parseToken(TokenType.RightParenthesis);
        } else if (parser.currentIs(TokenType.Identifier)) {
            Token token = parser.parseToken(TokenType.Identifier);
            String identifier = ((IdentifierToken)token).identifier;
            // This one is a bit weird because identifier is the start of both a
            // variable nod and a callnode, so we have to parse ahead a bit here
            if(parser.currentIs(TokenType.LeftParenthesis)) {
                type = Type.Call;
                call = new CallNode(identifier, CallNode.parseCallArgs(parser, visitor), visitor);
            } else {
                type = Type.Variable;
                variable = new VariableNode(identifier, VariableNode.parseArrayNotation(parser, visitor), visitor);
            }
        } else {
            parser.throwExpected(TokenType.IntegerLiteral, TokenType.LeftParenthesis, TokenType.Identifier);
        }
        visitor.accept(this);
    }
}