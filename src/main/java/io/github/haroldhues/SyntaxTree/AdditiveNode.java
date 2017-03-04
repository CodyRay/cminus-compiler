package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;
public class AdditiveNode extends SyntaxTreeNode {
    public AdditiveNode additive;
    public Token operation;
    public TermNode term;

    public AdditiveNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        additive = null;
        operation = null;
        term = new TermNode(parser, visitor);
        while(parser.currentToken().isAddOrSubtractOperator()) {
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            additive = new AdditiveNode(additive, operation, term);
            visitor.accept(additive); // Manually visit since it was manually created
            operation = parser.currentToken();
            parser.moveNextToken();
            term = new TermNode(parser, visitor);
        }
        
        visitor.accept(this);
    }

    public AdditiveNode(TermNode term) {
        this.term = term;
    }

    public AdditiveNode(AdditiveNode additive, Token operation, TermNode term) {
        this.additive = additive;
        this.operation = operation;
        this.term = term;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(operation != null) {
            builder.append(additive);
            builder.append(' ');
            builder.append(operation);
        }
        builder.append(term);
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof AdditiveNode)) {
            return false;
        }
         
        AdditiveNode that = (AdditiveNode) other;
 
        return this.operation.equals(that.operation) &&
            this.term.equals(that.term) &&
            this.additive.equals(that.additive);
    }
}