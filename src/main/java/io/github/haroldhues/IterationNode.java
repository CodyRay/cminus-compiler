package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class IterationNode extends SyntaxTree {
    public IterationNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}