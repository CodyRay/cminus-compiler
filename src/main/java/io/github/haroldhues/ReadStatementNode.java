package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class ReadStatementNode extends SyntaxTree {
    public ReadStatementNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}