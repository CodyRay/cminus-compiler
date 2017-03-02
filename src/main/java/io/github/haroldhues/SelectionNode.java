package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class SelectionNode extends SyntaxTree {
    public SelectionNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}