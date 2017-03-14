package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;


import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public abstract class ExpressionNode extends SyntaxTreeNode {
    public enum Type {
        NestedExpression, 
        Variable, 
        Call, 
        Literal, 
        BinaryOperator
    }

    public static ExpressionNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        // There is some ambiguity between `var = expression` and `simple-expression`
        // which also can be just `var`. To resolve this we assume that it is a simple
        // expression until we see the assignment operator
        ExpressionNode expression = parseComparableExpressionNode(parser, visitor);
        if(parser.currentIs(TokenType.Assign)) {
            if(expression.expressionType() != Type.Variable) {
                throw new CompileErrorException("The left hand of an assignment must be a variable reference", parser.currentToken().getLine(), parser.currentToken().getColumn());
            }
            ExpressionNode variable = expression;
            Token assignment = parser.parseToken(TokenType.Assign);
            expression = ExpressionNode.parse(parser, visitor);
            expression = new BinaryExpressionNode(variable, assignment, expression);
            visitor.accept(expression); // Since it was manually created
        }
        return expression;
    }
     
    public static ExpressionNode parseComparableExpressionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        ExpressionNode expression = parseAdditiveNode(parser, visitor);
        if(parser.currentToken().isCompareOperator()) {
            Token compare = parser.currentToken();
            // Already been looked at for the comparison
            parser.moveNextToken();
            ExpressionNode rightExpression = parseAdditiveNode(parser, visitor);
            expression = new BinaryExpressionNode(expression, compare, rightExpression);
            visitor.accept(expression); // Manually visit since it was manually created
        }
        return expression;
    }

    public static ExpressionNode parseAdditiveNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        ExpressionNode expression = parseTermNode(parser, visitor);
        while(parser.currentToken().isAddOrSubtractOperator()) {
            Token operation = parser.currentToken();
            parser.moveNextToken();
            ExpressionNode term = parseTermNode(parser, visitor);
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            expression = new BinaryExpressionNode(expression, operation, term);
            visitor.accept(expression); // Manually visit since it was manually created
        }
        return expression;
    }
    
    public static ExpressionNode parseTermNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        ExpressionNode expression = parseFactorNode(parser, visitor); // Singleton
        while(parser.currentToken().isMultiplyOrDivideOperator()) {
            Token operation = parser.currentToken();
            parser.moveNextToken();
            ExpressionNode factor = parseFactorNode(parser, visitor);
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            expression = new BinaryExpressionNode(expression, operation, factor);
            visitor.accept(expression); // Manually do this accept since we manually created the node here
        }
        return expression;
    }

    public static ExpressionNode parseFactorNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        if(parser.currentIs(TokenType.IntegerLiteral)) {
            return LiteralExpressionNode.parse(parser, visitor);
        } else if (parser.parseTokenIf(TokenType.LeftParenthesis)) {
            ExpressionNode expression = new NestedExpressionNode(ExpressionNode.parse(parser, visitor));
            parser.parseToken(TokenType.RightParenthesis);
            return expression;
        } else if (parser.currentIs(TokenType.Identifier)) {
            Token token = parser.parseToken(TokenType.Identifier);
            String identifier = ((IdentifierToken)token).identifier;
            // This one is a bit weird because identifier is the start of both a
            // variable nod and a callnode, so we have to parse ahead a bit here
            if(parser.currentIs(TokenType.LeftParenthesis)) {
                CallExpressionNode call = new CallExpressionNode(identifier, CallExpressionNode.parseCallArgs(parser, visitor));
                visitor.accept(call); // Manually accept because we manually created
                return call;
            } else {
                VariableExpressionNode variable = new VariableExpressionNode(identifier, VariableExpressionNode.parseArrayNotation(parser, visitor));
                visitor.accept(variable); // Manually accept because we manually created
                return variable;
            }
        } else {
            parser.throwExpected(TokenType.IntegerLiteral, TokenType.LeftParenthesis, TokenType.Identifier);
            return null; //Unreachable
        }
    }

    public abstract ExpressionNode.Type expressionType();
}