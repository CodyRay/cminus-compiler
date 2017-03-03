package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;





public class AdditiveNode extends SyntaxTreeNode {
    public AdditiveNode additive;
    public Operation operation;
    public TermNode term;

    public enum Operation {
        Add,
        Subtract,
        None, // No Left Node present
    }

    public AdditiveNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        additive = null;
        operation = Operation.None;
        term = new TermNode(parser, visitor);
        while(parser.currentIs(TokenType.Add) || parser.currentIs(TokenType.Subtract)) {
            // move what we have already parsed deeper in the tree so we
            // get left associativity           
            additive = new AdditiveNode(additive, operation, term, visitor);
            operation = getOperationFromToken(parser.currentToken());
            term = new TermNode(parser, visitor);
        }
        
        visitor.accept(this);
    }

    public AdditiveNode(TermNode term, Consumer<SyntaxTreeNode> visitor) {
        operation = Operation.None;
        this.term = term;
        visitor.accept(this);
    }

    public AdditiveNode(AdditiveNode additive, Operation operation, TermNode term, Consumer<SyntaxTreeNode> visitor) {
        this.additive = additive;
        this.operation = operation;
        this.term = term;
        visitor.accept(this);
    }

    public static Operation getOperationFromToken(Token token) throws Exception {
        if(token.type == TokenType.Add) {
            return Operation.Add;
        } else if(token.type == TokenType.Subtract) {
            return Operation.Subtract;
        }
        throw new Exception("Must be a add or subtract token");
    }
}