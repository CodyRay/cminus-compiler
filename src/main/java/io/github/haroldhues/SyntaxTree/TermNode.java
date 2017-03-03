package io.github.haroldhues.SyntaxTree;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class TermNode extends SyntaxTreeNode {
    public TermNode term;
    public Operation operation;
    public FactorNode factor;

    public enum Operation {
        Multiply,
        Divide,
        None, // No Left Node present
    }

    public TermNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        term = null;
        operation = Operation.None;
        factor = new FactorNode(parser, visitor);
        while(parser.currentIs(TokenType.Multiply) || parser.currentIs(TokenType.Divide)) {
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            term = new TermNode(term, operation, factor, visitor);
            operation = getOperationFromToken(parser.currentToken());
            factor = new FactorNode(parser, visitor);
        }
        
        visitor.accept(this);
    }

    public TermNode(FactorNode factor, Consumer<SyntaxTreeNode> visitor) {
        operation = Operation.None;
        this.factor = factor;
        
        visitor.accept(this);
    }

    public TermNode(TermNode term, Operation operation, FactorNode factor, Consumer<SyntaxTreeNode> visitor) {
        this.term = term;
        this.operation = operation;
        this.factor = factor;
        
        visitor.accept(this);
    }

    public static Operation getOperationFromToken(Token token) throws Exception {
        if(token.type == TokenType.Multiply) {
            return Operation.Multiply;
        } else if(token.type == TokenType.Divide) {
            return Operation.Divide;
        }
        throw new Exception("Must be a multiply or divide token");
    }
}