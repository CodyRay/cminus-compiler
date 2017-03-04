package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class ProgramSyntaxNode extends SyntaxTreeNode {
    public List<DeclarationSyntaxNode> declarationList = new ArrayList<DeclarationSyntaxNode>();
    
    public ProgramSyntaxNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        while(!parser.parseTokenIf(TokenType.Eof)) {
            declarationList.add(new DeclarationSyntaxNode(parser, visitor));
        }

        visitor.accept(this);
    }

    public ProgramSyntaxNode(List<DeclarationSyntaxNode> declarationList) {
        this.declarationList = declarationList;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(DeclarationSyntaxNode syntax : declarationList) {
            builder.append(syntax.toString());
            builder.append('\n');
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        
        if (!(other instanceof ProgramSyntaxNode)) {
            return false;
        }
         
        ProgramSyntaxNode that = (ProgramSyntaxNode) other;
 
        return this.declarationList.equals(that.declarationList);
    }
}