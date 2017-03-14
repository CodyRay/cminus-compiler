package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.function.Consumer;

import io.github.haroldhues.CompileErrorException;
import io.github.haroldhues.Parser;
import io.github.haroldhues.Tokens.Token;
import io.github.haroldhues.Tokens.TokenType;

import java.util.ArrayList;





public class CompoundStatementNode extends StatementNode {

    public List<DeclarationNode> localDeclarations = new ArrayList<DeclarationNode>();
    public List<StatementNode> statements = new ArrayList<StatementNode>();

    public static CompoundStatementNode parse(Parser parser, Consumer<SyntaxTreeNode> visitor) throws CompileErrorException {
        List<DeclarationNode> localDeclarations = new ArrayList<DeclarationNode>();
        List<StatementNode> statements = new ArrayList<StatementNode>();
        parser.parseToken(TokenType.LeftBrace);
        // Local Declarations
        while(parser.currentIs(TokenType.Int) || parser.currentIs(TokenType.Void)) {
            Token start = parser.currentToken();
            // This will allow function declarations inside functions
            // but we will check for this and cause errors for that.
            DeclarationNode declaration = DeclarationNode.parse(parser, visitor);
            if(declaration.type == DeclarationNode.Type.Function) {
                throw new CompileErrorException("Functions cannot be declared inside of functions", start.getLine(), start.getColumn());
            }

            localDeclarations.add(declaration);
        }
        // Statements
        while(!parser.parseTokenIf(TokenType.RightBrace)) {
            statements.add(StatementNode.parse(parser, visitor));
        }
        
        CompoundStatementNode statement = new CompoundStatementNode(localDeclarations, statements);
        visitor.accept(statement);
        return statement;
    }

    public CompoundStatementNode(List<DeclarationNode> localDeclarations, List<StatementNode> statements) {
        this.localDeclarations = localDeclarations;
        this.statements = statements;
    }

    public StatementNode.Type statementType() {
        return StatementNode.Type.Compound;
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