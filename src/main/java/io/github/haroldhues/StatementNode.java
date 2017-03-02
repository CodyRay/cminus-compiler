package io.github.haroldhues;

public class StatementNode extends SyntaxTree {
    public enum Type {
        Expression,
        Compound,
        Selection,
        Iteration,
        Return,
        Read,
        Write
    }

    public SyntaxTree statementNode;

    public StatementNode(Parser parser) throws Exception {
        if(parser.currentIs(TokenType.Write)) {
            statementNode = new WriteStatementNode(parser);
        } else if(parser.currentIs(TokenType.Read)) {
            statementNode = new ReadStatementNode(parser);
        } else if(parser.currentIs(TokenType.Return)) {
            statementNode = new ReturnStatementNode(parser);
        } else if(parser.currentIs(TokenType.While)) {
            statementNode = new IterationNode(parser);
        } else if(parser.currentIs(TokenType.If)) {
            statementNode = new SelectionNode(parser);
        } else if(parser.currentIs(TokenType.LeftBrace)) {
            statementNode = new CompoundStatementNode(parser);
        } else {
            statementNode = new ExpressionStatementNode(parser);
        }
        parser.visit(this);
    }
}