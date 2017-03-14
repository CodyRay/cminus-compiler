package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

import java.util.ArrayList;





public class CallExpressionNode extends ExpressionNode {
    public String identifier;
    public List<ExpressionNode> arguments;

    public static CallExpressionNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        String identifier = ((IdentifierToken)parser.parseToken(TokenType.Identifier)).identifier;
        List<ExpressionNode> arguments = parseCallArgs(parser, visitor);
        
        CallExpressionNode expression = new CallExpressionNode(identifier, arguments);
        visitor.accept(expression);
        return expression;
    }

    public CallExpressionNode(String identifier, List<ExpressionNode> arguments) {
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public static List<ExpressionNode> parseCallArgs(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();
        parser.parseToken(TokenType.LeftParenthesis);
        if(!parser.parseTokenIf(TokenType.RightParenthesis)) {
            do {
                args.add(ExpressionNode.parse(parser, visitor));
            } while(parser.parseTokenIf(TokenType.Comma));
            parser.parseToken(TokenType.RightParenthesis);
        }
        return args;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(identifier);
        builder.append(new Token(TokenType.LeftParenthesis).toString().trim());
        String delimiter = "";
        for(ExpressionNode arg: arguments) {
            builder.append(delimiter);
            builder.append(arg.toString().trim());
            delimiter = new Token(TokenType.Comma).toString();
        }
        builder.append(new Token(TokenType.RightParenthesis));
        return builder.toString();
    }

    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.identifier)
			.property(o -> o.arguments)
			.result(this, other);
    }

    public ExpressionNode.Type expressionType() {
        return ExpressionNode.Type.Call;
    }
}