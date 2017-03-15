package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import io.github.haroldhues.CompileErrorException;





public abstract class SyntaxTreeVisitor
{
    public void accept(RootNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(DeclarationNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(TypeSpecifierNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(ParameterDeclarationNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(CompoundStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(WriteStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(ReadStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(ReturnStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(IterationStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(SelectionStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(ExpressionStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(AssignmentExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(VariableExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(NestedExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(CallExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(LiteralExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(BinaryExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    @FunctionalInterface
    public interface Nextable {
        public void run() throws CompileErrorException;
    }
}