package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class WriteStatementNode extends SyntaxTree {
    public WriteStatementNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}