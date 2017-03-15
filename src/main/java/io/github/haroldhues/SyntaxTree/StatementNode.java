package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;





public abstract class StatementNode extends SyntaxTreeNode {
    public enum Type {
        Expression,
        Compound,
        Selection,
        Iteration,
        Return,
        Read,
        Write
    }

    public abstract Type statementType();
    public abstract boolean allPathsReturn();

    public static StatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        if(parser.currentIs(TokenType.Write)) {
            return WriteStatementNode.parse(parser, visitor);
        } else if(parser.currentIs(TokenType.Read)) {
            return ReadStatementNode.parse(parser, visitor);
        } else if(parser.currentIs(TokenType.Return)) {
            return ReturnStatementNode.parse(parser, visitor);
        } else if(parser.currentIs(TokenType.While)) {
            return IterationStatementNode.parse(parser, visitor);
        } else if(parser.currentIs(TokenType.If)) {
            return SelectionStatementNode.parse(parser, visitor);
        } else if(parser.currentIs(TokenType.LeftBrace)) {
            return CompoundStatementNode.parse(parser, visitor);
        } else {
            return ExpressionStatementNode.parse(parser, visitor);
        }
    }
}