package io.github.haroldhues;

import java.util.ArrayList;
import java.util.List;

import io.github.haroldhues.SymbolTable.Entry;
import io.github.haroldhues.SymbolTable.ArrayVariableEntry;
import io.github.haroldhues.SymbolTable.FunctionEntry;
import io.github.haroldhues.SymbolTable.VariableEntry;
import io.github.haroldhues.SymbolTable.FunctionEntry.ReturnType;



import io.github.haroldhues.SyntaxTree.*;
import io.github.haroldhues.SyntaxTree.ExpressionNode.ResultType;

public class Checker extends SyntaxTreeVisitor {
    private SymbolTable symbolTable = SymbolTable.root();
    private DeclarationNode currentFunctionDeclaration = null;

    public void accept(RootNode node, Nextable next) throws CompileErrorException {
        // Create the 'global' scope
        symbolTable = new SymbolTable(symbolTable);
        node.symbolTable = symbolTable;
        next.run();
        DeclarationNode last = node.declarationList.size() > 0 ? node.declarationList.get(node.declarationList.size() - 1) : null;
        if (last == null ||
            last.type != DeclarationNode.Type.Function || 
            last.typeSpecifier.type != TypeSpecifierNode.Type.Void ||
            last.identifier != "main" ||
            last.functionParameters.size() != 0) {

            throw new CompileErrorException("The last declaration in a program must be a function declaration of the form `void main(void)`");
        }
        symbolTable = symbolTable.getOuterScope();
    }

    public void accept(DeclarationNode node, Nextable next) throws CompileErrorException {
        Entry entry = null;
        switch(node.type) {
            case Variable:
                // Verify that type is int
                if (node.typeSpecifier.type != TypeSpecifierNode.Type.Int) {
                    throw new CompileErrorException("The type of variables must be integers", node.typeSpecifier.getLocation());
                }
                entry = new VariableEntry(node.getLocation(), node.identifier);
                break;
            case ArrayVariable:
                // Verify that base type is int
                if (node.typeSpecifier.type != TypeSpecifierNode.Type.Int) {
                    throw new CompileErrorException("The base type of variable arrays must be integer", node.typeSpecifier.getLocation());
                }
                // Verify that integer size is >0 (we have no negative numbers)
                if (node.arraySize == 0) {
                    throw new CompileErrorException("The array size cannot be zero", node.getLocation());
                }
                entry = new ArrayVariableEntry(node.getLocation(), node.identifier, node.arraySize);
                break;
            case Function:
                currentFunctionDeclaration = node;
                // Verify that all code paths of body return if the return type is not int
                if (node.typeSpecifier.type == TypeSpecifierNode.Type.Int && !node.functionBody.allPathsReturn()) {
                    throw new CompileErrorException("All paths in '" + node.identifier + "' must return an integer", node.getLocation());
                }
                ReturnType returnType = node.typeSpecifier.type == TypeSpecifierNode.Type.Int ? 
                    ReturnType.Int : 
                    ReturnType.Void;
                List<FunctionEntry.Parameter> parameters = new ArrayList<FunctionEntry.Parameter>();
                for(ParameterDeclarationNode param : node.functionParameters) {
                    parameters.add(new FunctionEntry.Parameter(param.identifier, param.isArray));
                }
                entry = new FunctionEntry(node.getLocation(), node.identifier, returnType, parameters);
                break;
        }
        symbolTable.insert(node.identifier, entry);
        next.run();
    }

    public void accept(CompoundStatementNode node, Nextable next) throws CompileErrorException {
        // Create a new scope for this block
        symbolTable = new SymbolTable(symbolTable);
        node.symbolTable = symbolTable;
        // Add the parameters to this scope if it is the function body of the current function
        if(node == currentFunctionDeclaration.functionBody) {
            for(ParameterDeclarationNode parameter: currentFunctionDeclaration.functionParameters) {
                symbolTable.insert(parameter.identifier, parameter.isArray ? 
                    new ArrayVariableEntry(parameter.getLocation(), parameter.identifier, null) : 
                    new VariableEntry(parameter.getLocation(), parameter.identifier));
            }
        }
        next.run();
        // Remove this scope now that we have processed the block
        symbolTable = symbolTable.getOuterScope();
    }

    public void accept(WriteStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
        if(node.expression.resultType != ResultType.Integer) {
            throw new CompileErrorException("Only integers can be used with 'write'", node.getLocation());
        }
    }

    public void accept(ReadStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
        if(node.reference.resultType != ResultType.Integer) {
            throw new CompileErrorException("The variable for a read statement must accept assignment of an integer", node.getLocation());
        }
    }

    public void accept(ReturnStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
        if(currentFunctionDeclaration.typeSpecifier.type == TypeSpecifierNode.Type.Int) {
            // Verify that values are returned for int functions
            if(node.expression == null || node.expression.resultType != ExpressionNode.ResultType.Integer) {
                throw new CompileErrorException("All return statements in '" + currentFunctionDeclaration.identifier + "' must return integers", node.getLocation());
            }
        } else {
            // Verify that no value is returned for void functions
            if(node.expression != null) {
                throw new CompileErrorException("Return statements in '" + currentFunctionDeclaration.identifier + "' cannot return values", node.expression.getLocation());
            }
        }
    }

    public void accept(IterationStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
        // Verify condition is not void
        if(node.condition.resultType == ExpressionNode.ResultType.Void) {
            throw new CompileErrorException("The condition for a while loop cannot be void", node.condition.getLocation());
        }
    }

    public void accept(SelectionStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
        // Verify condition is not void
        if(node.condition.resultType == ExpressionNode.ResultType.Void) {
            throw new CompileErrorException("The condition for a if statement cannot be void", node.condition.getLocation());
        }
    }

    public void accept(ExpressionStatementNode node, Nextable next) throws CompileErrorException {
        next.run();
    }

    public void accept(AssignmentExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        // Verify that right expression is int
        if(node.expression.resultType != ExpressionNode.ResultType.Integer) {
            throw new CompileErrorException("Only integers can be assigned to variables", node.expression.getLocation());
        }
        if(node.variable.resultType != ExpressionNode.ResultType.Integer) {
            throw new CompileErrorException("Only simple variables or subscripted arrays variables can be the left hand of an expression", node.variable.getLocation());
        }
    }

    public void accept(VariableExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        Entry entry = symbolTable.get(node.identifier, node.getLocation());
        if(entry.getType() == Entry.Type.Function) {
            throw new CompileErrorException("Cannot use function, '" + entry.identifier +"', as a variable", node.getLocation());
        }
        
        if (entry.getType() == Entry.Type.ArrayVariable) {
            node.resultType = node.arrayExpression == null ? ResultType.IntegerArray : ResultType.Integer;

            if(node.arrayExpression != null && node.arrayExpression.resultType != ResultType.Integer) {
                // Verify the expression for index results in an integer
                throw new CompileErrorException("Array variables can only be indexed using integers", node.arrayExpression.getLocation());
            }
        } else {
            node.resultType = ResultType.Integer;
            if(node.arrayExpression != null) {
                throw new CompileErrorException("Non-array variables cannot be used as arrays", node.getLocation());
            }
        }
    }

    public void accept(NestedExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        node.resultType = node.expression.resultType;
    }

    public void accept(CallExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        Entry entry = symbolTable.get(node.identifier, node.getLocation());

        if(entry.getType() != Entry.Type.Function) {
            throw new CompileErrorException("Cannot invoke the variable, '" + entry.identifier + "' as a function", node.getLocation());
        }

        FunctionEntry functionDeclaration = (FunctionEntry)entry;
        if(functionDeclaration.parameters.size() != node.arguments.size()) {
            throw new CompileErrorException("Function, '" + entry.identifier + "' recieved " +node.arguments.size() + " arguments, but " + functionDeclaration.parameters.size() + " were expected", node.getLocation());
        }

        for(int x = 0; x < node.arguments.size(); x ++) {
            FunctionEntry.Parameter declared = functionDeclaration.parameters.get(x);
            ExpressionNode argument = node.arguments.get(x);
            if (argument.resultType == ResultType.Void) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' recieved an expression that evaluated to void", argument.getLocation());
            }
            if(declared.isArray && argument.resultType == ResultType.Integer) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' recieved an expression that evaluated to an integer, but an integer array was expected", argument.getLocation());
            }
            if(!declared.isArray && argument.resultType == ResultType.IntegerArray) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' recieved an expression that evaluated to an integer array, but an integer was expected", argument.getLocation());
            }
        }

        node.resultType = functionDeclaration.returnType == ReturnType.Int ? ResultType.Integer : ResultType.Void;
    }

    public void accept(LiteralExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        node.resultType = ExpressionNode.ResultType.Integer;
    }

    public void accept(BinaryExpressionNode node, Nextable next) throws CompileErrorException {
        next.run();
        if(node.left.resultType != ExpressionNode.ResultType.Integer || node.right.resultType != ExpressionNode.ResultType.Integer) {
            throw new CompileErrorException("Both sides of an " + node.operation.type.toString().toLowerCase() + " expression must result in integers", node.getLocation());
        }
    }
}