package io.github.haroldhues.SyntaxTree;

import java.util.List;
import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

import java.util.ArrayList;





public class CallExpressionNode extends ExpressionNode {
    public String identifier;
    public List<ExpressionNode> arguments;

    public CallExpressionNode(Location location, String identifier, List<ExpressionNode> arguments) {
    	super(location);
        this.identifier = identifier;
        this.arguments = arguments;
    }

    public static List<ExpressionNode> parseCallArgs(Parser parser) throws CompileErrorException {
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();
        parser.parseToken(TokenType.LeftParenthesis);
        if(!parser.parseTokenIf(TokenType.RightParenthesis)) {
            do {
                args.add(ExpressionNode.parse(parser));
            } while(parser.parseTokenIf(TokenType.Comma));
            parser.parseToken(TokenType.RightParenthesis);
        }
        return args;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            for(ExpressionNode argument: arguments) {
                SyntaxTreeNode.visit(argument, visitor);
            }
        });
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