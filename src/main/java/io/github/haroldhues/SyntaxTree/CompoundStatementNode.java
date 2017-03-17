package io.github.haroldhues.SyntaxTree;

import java.util.List;
import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.InputOutput;
import io.github.haroldhues.Location;
import io.github.haroldhues.Parser;
import io.github.haroldhues.SymbolTable;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class CompoundStatementNode extends StatementNode {
    // Attributes
    public SymbolTable symbolTable = null;

    // Children
    public List<DeclarationNode> localDeclarations = new ArrayList<DeclarationNode>();
    public List<StatementNode> statements = new ArrayList<StatementNode>();

    public static CompoundStatementNode parse(Parser parser) throws CompileErrorException {
        Location location = parser.currentLocation();
        List<DeclarationNode> localDeclarations = new ArrayList<DeclarationNode>();
        List<StatementNode> statements = new ArrayList<StatementNode>();
        parser.parseToken(TokenType.LeftBrace);
        // Local Declarations
        while(parser.currentIs(TokenType.Int) || parser.currentIs(TokenType.Void)) {
            Token start = parser.currentToken();
            // This will allow function declarations inside functions
            // but we will check for this and cause errors for that.
            DeclarationNode declaration = DeclarationNode.parse(parser);
            if(declaration.type == DeclarationNode.Type.Function) {
                throw new CompileErrorException("Functions cannot be declared inside of functions", start.getLocation());
            }

            localDeclarations.add(declaration);
        }
        // Statements
        while(!parser.parseTokenIf(TokenType.RightBrace)) {
            statements.add(StatementNode.parse(parser));
        }
        
        return new CompoundStatementNode(location, localDeclarations, statements);
    }

    public CompoundStatementNode(Location location, List<DeclarationNode> localDeclarations, List<StatementNode> statements) {
    	super(location);
        this.localDeclarations = localDeclarations;
        this.statements = statements;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Compound;
    }

    public void visit(SyntaxTreeVisitor visitor) throws CompileErrorException {
        visitor.accept(this, () -> {
            for(DeclarationNode declaration: localDeclarations) {
                SyntaxTreeNode.visit(declaration, visitor);
            }
            for(StatementNode statement: statements) {
                SyntaxTreeNode.visit(statement, visitor);
            }
        });
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(new Token(TokenType.LeftBrace));
        builder.append('\n');
        
        StringBuilder indented = new StringBuilder();
        for(DeclarationNode declaration : localDeclarations) {
            indented.append(declaration);
            indented.append('\n');
        }
        for(StatementNode statement : statements) {
            indented.append(statement);
            indented.append('\n');
        }
        for(String line : indented.toString().split("\n")) {
            builder.append("    ");
            builder.append(line);
            builder.append('\n');
        }

        builder.append(new Token(TokenType.RightBrace));
        return builder.toString();
    }

    public boolean equals(Object other) {
		return equalsBuilder(this)
			.property(o -> o.localDeclarations)
			.property(o -> o.statements)
			.result(this, other);
    }
}