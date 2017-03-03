package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class CompoundStatementNode extends SyntaxTreeNode {

    public List<DeclarationSyntaxNode> localDeclarations = new ArrayList<DeclarationSyntaxNode>();
    public List<StatementNode> statements = new ArrayList<StatementNode>();

    public CompoundStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws Exception {
        parser.parseToken(TokenType.LeftBrace);
        // Local Declarations
        while(parser.currentIs(TokenType.Int) || parser.currentIs(TokenType.Void)) { 
            // TODO: This will allow function declarations inside functions
            // the checker should check for this and cause errors for that.
            localDeclarations.add(new DeclarationSyntaxNode(parser, visitor));
        }
        // Statements
        while(!parser.parseTokenIf(TokenType.RightBrace)) {
            statements.add(new StatementNode(parser, visitor));
        }
        visitor.accept(this);
    }
}