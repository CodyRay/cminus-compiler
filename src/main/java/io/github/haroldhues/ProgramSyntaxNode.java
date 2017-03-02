package io.github.haroldhues;

import java.util.ArrayList;
import java.util.List;



public class ProgramSyntaxNode extends SyntaxTree {
    public List<DeclarationSyntaxNode> declarationList = new ArrayList()<DeclarationSyntaxNode>();
    
    public ProgramSyntaxNode(Parser parser) {
        while(!parser.parseTokenIf(TokenType.Eof)) {
            declarationList.add(parser.visit(new DeclarationSyntaxNode(parser)));
        }
        parser.visit(this);
    }
}