package io.github.haroldhues.SyntaxTree;

import java.util.ArrayList;
import java.util.List;
import io.github.haroldhues.*;
import io.github.haroldhues.Tokens.*;

public class RootNode extends SyntaxTreeNode {
    // Attributes
    public SymbolTable symbolTable = null;

    // Children
    public List<DeclarationNode> declarationList = new ArrayList<DeclarationNode>();
    
    public static RootNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        List<DeclarationNode> declarationList = new ArrayList<DeclarationNode>();
        while(!parser.parseTokenIf(TokenType.Eof)) {
            declarationList.add(DeclarationNode.parse(parser));
        }

        return new RootNode(location, declarationList);
    }

    public RootNode(Location location, List<DeclarationNode> declarationList) {
    	super(location);
        this.declarationList = declarationList;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            for(DeclarationNode declaration: declarationList) {
                SyntaxTreeNode.visit(declaration, visitor);
            }
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(DeclarationNode syntax : declarationList) {
        	if(syntax.type == DeclarationNode.Type.Function) {
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