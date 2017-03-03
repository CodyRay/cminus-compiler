package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;





public class ProgramSyntaxNode extends SyntaxTreeNode {
    public List<DeclarationSyntaxNode> declarationList = new ArrayList<DeclarationSyntaxNode>();
    
    public ProgramSyntaxNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        while(!parser.parseTokenIf(TokenType.Eof)) {
            declarationList.add(new DeclarationSyntaxNode(parser, visitor));
        }
        visitor.accept(this);
    }
}