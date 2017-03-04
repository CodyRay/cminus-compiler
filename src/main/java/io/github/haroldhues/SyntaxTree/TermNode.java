package io.github.haroldhues.SyntaxTree;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class TermNode extends SyntaxTreeNode {
    public TermNode term;
    public Token operation;
    public FactorNode factor;

    public TermNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        term = null;
        operation = null;
        factor = new FactorNode(parser, visitor);
        while(parser.currentToken().isMultiplyOrDivideOperator()) {
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            term = new TermNode(term, operation, factor);
            visitor.accept(term); // Manually do this accept since we created the node here
            operation = parser.currentToken();
            parser.moveNextToken();
            factor = new FactorNode(parser, visitor);
        }
        
        visitor.accept(this);
    }

    public TermNode(FactorNode factor) {
        this.factor = factor;
    }

    public TermNode(TermNode term, Token operation, FactorNode factor) {
        this.term = term;
        this.operation = operation;
        this.factor = factor;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(operation != null) {
            builder.append(term);
            builder.append(operation);
        }
        builder.append(factor);
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.operation)
			.property(o -> o.term)
			.property(o -> o.factor)
			.result(this, other);
    }
}