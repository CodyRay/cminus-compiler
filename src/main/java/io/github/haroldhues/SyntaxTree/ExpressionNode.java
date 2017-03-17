package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.SymbolTable;
import io.github.haroldhues.Tokens.IdentifierToken;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

public abstract class ExpressionNode extends SyntaxTreeNode {
    public enum Type {
        Assignment,
        Nested, 
        Variable, 
        Call, 
        Literal, 
        Binary,
    }
    
    protected ExpressionNode(Location location) {
    	super(location);
    }

    public static ExpressionNode parse(Parser parser) throws CompileErrorException {
        // There is some ambiguity between `var = expression` and `simple-expression`
        // which also can be just `var`. To resolve this we assume that it is a simple
        // expression until we see the assignment operator
        ExpressionNode expression = parseComparableExpressionNode(parser);
        if(parser.parseTokenIf(TokenType.Assign)) {
            if(expression.expressionType() != Type.Variable) {
                throw new CompileErrorException("The left hand of an assignment must be a variable reference", parser.currentToken().getLocation());
            }
            VariableExpressionNode variable = (VariableExpressionNode)expression;
            expression = ExpressionNode.parse(parser);
            expression = new AssignmentExpressionNode(variable.getLocation(), variable, expression);
        }
        return expression;
    }
     
    public static ExpressionNode parseComparableExpressionNode(Parser parser) throws CompileErrorException {
        ExpressionNode expression = parseAdditiveNode(parser);
        if(parser.currentToken().isCompareOperator()) {
            Token compare = parser.currentToken();
            // Already been looked at for the comparison
            parser.moveNextToken();
            ExpressionNode rightExpression = parseAdditiveNode(parser);
            expression = new BinaryExpressionNode(expression.getLocation(), expression, compare, rightExpression);
        }
        return expression;
    }

    public static ExpressionNode parseAdditiveNode(Parser parser) throws CompileErrorException {
        ExpressionNode expression = parseTermNode(parser);
        while(parser.currentToken().isAddOrSubtractOperator()) {
            Token operation = parser.currentToken();
            parser.moveNextToken();
            ExpressionNode term = parseTermNode(parser);
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            expression = new BinaryExpressionNode(expression.getLocation(), expression, operation, term);
        }
        return expression;
    }
    
    public static ExpressionNode parseTermNode(Parser parser) throws CompileErrorException {
        ExpressionNode expression = parseFactorNode(parser); // Singleton
        while(parser.currentToken().isMultiplyOrDivideOperator()) {
            Token operation = parser.currentToken();
            parser.moveNextToken();
            ExpressionNode factor = parseFactorNode(parser);
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            expression = new BinaryExpressionNode(expression.getLocation(), expression, operation, factor);
        }
        return expression;
    }

    public static ExpressionNode parseFactorNode(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        if(parser.currentIs(TokenType.IntegerLiteral)) {
            return LiteralExpressionNode.parse(parser);
        } else if (parser.parseTokenIf(TokenType.LeftParenthesis)) {
            ExpressionNode expression = new NestedExpressionNode(location, ExpressionNode.parse(parser));
            parser.parseToken(TokenType.RightParenthesis);
            return expression;
        } else if (parser.currentIs(TokenType.Identifier)) {
            Token token = parser.parseToken(TokenType.Identifier);
            String identifier = ((IdentifierToken)token).identifier;
            // This one is a bit weird because identifier is the start of both a
            // variable nod and a callnode, so we have to parse ahead a bit here
            if(parser.currentIs(TokenType.LeftParenthesis)) {
                CallExpressionNode call = new CallExpressionNode(location, identifier, CallExpressionNode.parseCallArgs(parser));
                return call;
            } else {
                VariableExpressionNode variable = new VariableExpressionNode(location, identifier, VariableExpressionNode.parseArrayNotation(parser));
                return variable;
            }
        } else {
            parser.throwExpected(TokenType.IntegerLiteral, TokenType.LeftParenthesis, TokenType.Identifier);
            return null; //Unreachable
        }
    }

    public abstract ExpressionNode.Type expressionType();
}