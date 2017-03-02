package io.github.haroldhues;

import javax.naming.OperationNotSupportedException;

public class ExpressionStatementNode extends SyntaxTree {
    public ExpressionStatementNode(Parser parser) throws Exception {
        parser.visit(this);
        throw new OperationNotSupportedException();
    }
}