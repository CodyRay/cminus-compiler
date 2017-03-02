package io.github.haroldhues;

import java.util.List;
import java.util.ArrayList;



public class CompoundStatementNode extends SyntaxTree {

    public List<DeclarationSyntaxNode> localDeclarations = new ArrayList<DeclarationSyntaxNode>();
    public List<StatementNode> statements = new ArrayList<StatementNode>();

    public CompoundStatementNode(Parser parser) throws Exception {
        parser.parseToken(TokenType.LeftBrace);
        // Local Declarations
        while(parser.currentIs(TokenType.Int) || parser.currentIs(TokenType.Void)) { 
            // TODO: This will allow function declarations inside functions
            // the checker should check for this and cause errors for that.
            localDeclarations.add(new DeclarationSyntaxNode(parser));
        }
        // Statements
        while(!parser.parseTokenIf(TokenType.RightBrace)) {
            statements.add(new StatementNode(parser));
        }
        parser.visit(this);
    }
}