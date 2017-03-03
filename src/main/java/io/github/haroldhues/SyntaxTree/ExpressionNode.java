package io.github.haroldhues.SyntaxTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;

public class ExpressionNode extends SyntaxTreeNode {
    public enum Type {
        Assignment,
        SimpleExpressionNode,
    }

    public Type type;
    public VariableNode assignmentVariable;
    public ExpressionNode assignmentExpression;
    public SimpleExpressionNode simpleExpressionNode;

    public ExpressionNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        VisitorBuffer visitCollector = new VisitorBuffer();
        // There is some ambiguity between `var = expression` and `simple-expression`
        // which also can be just `var`. To resolve this we assume that it is a simple
        // expression until we see the assignment operator
        SimpleExpressionNode expression = new SimpleExpressionNode(parser, visitCollector);
        if(parser.parseTokenIf(TokenType.Assign)) {
            if(expression.compare != SimpleExpressionNode.Comparison.None || 
               expression.left.operation != AdditiveNode.Operation.None ||
               expression.left.term.operation != TermNode.Operation.None ||
               expression.left.term.factor.type != FactorNode.Type.Variable) {
                throw new Exception("The left hand value of assignment must be a variable not an expression");
            }
            VariableNode capturedNode = expression.left.term.factor.variable;
            // Recreate the node so that it is visited
            type = Type.Assignment;
            assignmentVariable = new VariableNode(capturedNode.identifier, capturedNode.arrayExpression, visitor);
            assignmentExpression = new ExpressionNode(parser, visitor);
        } else {
            visitCollector.replay(visitor); // Now that the order is guarenteed

            type = Type.SimpleExpressionNode;
            simpleExpressionNode = expression;
        }
        
        visitor.accept(this);
    }

    public class VisitorBuffer implements Consumer<SyntaxTreeNode> {
        public Queue<SyntaxTreeNode> nodes = new LinkedList<SyntaxTreeNode>();
        public void accept(SyntaxTreeNode node) {
            nodes.add(node);
        }

        public void replay(Consumer<SyntaxTreeNode> visitor) {
            while(nodes.size() > 0) {
                visitor.accept(nodes.remove());
            }
        }
    }
}