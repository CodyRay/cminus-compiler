package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class ProgramSyntaxNode extends SyntaxTreeNode {
    public List<DeclarationSyntaxNode> declarationList = new ArrayList<DeclarationSyntaxNode>();
    
    public ProgramSyntaxNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
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
        	if(syntax.type == DeclarationSyntaxNode.Type.Function) {
        		builder.append("\n");
        	}
            builder.append(syntax.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
    
    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.declarationList)
			.result(this, other);
    }
}