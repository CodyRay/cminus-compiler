package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class CompoundStatementNode extends SyntaxTreeNode {

    public List<DeclarationNode> localDeclarations = new ArrayList<DeclarationNode>();
    public List<StatementNode> statements = new ArrayList<StatementNode>();

    public CompoundStatementNode(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        parser.parseToken(TokenType.LeftBrace);
        // Local Declarations
        while(parser.currentIs(TokenType.Int) || parser.currentIs(TokenType.Void)) { 
            // TODO: This will allow function declarations inside functions
            // the checker should check for this and cause errors for that.
            localDeclarations.add(new DeclarationNode(parser, visitor));
        }
        // Statements
        while(!parser.parseTokenIf(TokenType.RightBrace)) {
            statements.add(new StatementNode(parser, visitor));
        }
        visitor.accept(this);
    }

    public CompoundStatementNode(List<DeclarationNode> localDeclarations, List<StatementNode> statements) {
        this.localDeclarations = localDeclarations;
        this.statements = statements;
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