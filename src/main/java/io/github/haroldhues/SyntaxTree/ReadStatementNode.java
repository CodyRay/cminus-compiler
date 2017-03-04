package io.github.haroldhues.SyntaxTree;

import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;

public class ReadStatementNode extends SyntaxTreeNode {
    public VariableNode reference;
    public ReadStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.Read);
        if(parser.parseTokenIf(TokenType.LeftParenthesis)) {
            reference = new VariableNode(parser, visitor);
            parser.parseToken(TokenType.RightParenthesis);
        } else {
            reference = new VariableNode(parser, visitor);
        }
        parser.parseToken(TokenType.Semicolon);
        visitor.accept(this);
    }

    public ReadStatementNode(VariableNode reference) {
        this.reference = reference;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("read ");
        builder.append(reference.toString());
        builder.append(";");
        return builder.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ReadStatementNode)) {
            return false;
        }
         
        ReadStatementNode that = (ReadStatementNode) other;
 
        return this.reference.equals(that.reference);
    }
}