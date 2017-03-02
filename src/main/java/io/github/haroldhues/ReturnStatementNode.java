package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class ReturnStatementNode extends SyntaxTree {
    public ReturnStatementNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}