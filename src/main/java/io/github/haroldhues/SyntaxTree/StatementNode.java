package io.github.haroldhues.SyntaxTree;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.InputOutput;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.SymbolTable;
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
    
    protected StatementNode(Location location) {
    	super(location);
    }

    public abstract Type statementType(); 

    public static StatementNode parse(Parser parser) throws CompileErrorException {
        if(parser.currentIs(TokenType.Write)) {
            return WriteStatementNode.parse(parser);
        } else if(parser.currentIs(TokenType.Read)) {
            return ReadStatementNode.parse(parser);
        } else if(parser.currentIs(TokenType.Return)) {
            return ReturnStatementNode.parse(parser);
        } else if(parser.currentIs(TokenType.While)) {
            return IterationStatementNode.parse(parser);
        } else if(parser.currentIs(TokenType.If)) {
            return SelectionStatementNode.parse(parser);
        } else if(parser.currentIs(TokenType.LeftBrace)) {
            return CompoundStatementNode.parse(parser);
        } else {
            return ExpressionStatementNode.parse(parser);
        }
    }
}