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
            if(expression.compare != null || 
               expression.left.operation != null ||
               expression.left.term.operation != null ||
               expression.left.term.factor.type != FactorNode.Type.Variable) {
                throw new Exception("The left hand value of assignment must be a variable not an expression");
            }
            type = Type.Assignment;
            VariableNode capturedNode = expression.left.term.factor.variable;
            // Becasuse we didn't visit when building the `SimpleExpressionNode`
            visitor.accept(capturedNode);
            assignmentVariable = capturedNode;
            assignmentExpression = new ExpressionNode(parser, visitor);
        } else {
            visitCollector.replay(visitor); // Now that the order is guarenteed

            type = Type.SimpleExpressionNode;
            simpleExpressionNode = expression;
        }
        
        visitor.accept(this);
    }

    public ExpressionNode(VariableNode var, ExpressionNode assignment) {
        type = Type.Assignment;
        assignmentVariable = var;
        assignmentExpression = assignment;
    }

    public ExpressionNode(SimpleExpressionNode expression) {
        type = Type.SimpleExpressionNode;
        simpleExpressionNode = expression;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(type == Type.Assignment) {
            builder.append(assignmentVariable);
            builder.append(" = ");
            builder.append(assignmentExpression);
        } else {
            builder.append(simpleExpressionNode);
        }
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ExpressionNode)) {
            return false;
        }
         
        ExpressionNode that = (ExpressionNode) other;
 
        return this.type.equals(that.type) &&
            this.assignmentVariable.equals(that.assignmentVariable) &&
            this.assignmentExpression.equals(that.assignmentExpression) &&
            this.simpleExpressionNode.equals(that.simpleExpressionNode);
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