package io.github.haroldhues;

import java.util.ArrayList;
import java.util.List;

import io.github.haroldhues.SymbolTable.*;
import io.github.haroldhues.SymbolTable.FunctionEntry.ReturnType;

import io.github.haroldhues.SyntaxTree.*;

public class Checker {
    public static void check(RootNode rootNode) throws CompileErrorException {
        SymbolTable symbolTable = new SymbolTable(SymbolTable.newGlobal());
        rootNode.symbolTable = symbolTable;

        for(DeclarationNode declaration: rootNode.declarationList) {
            check(symbolTable, declaration);
        }

        DeclarationNode last = rootNode.declarationList.size() > 0 ? rootNode.declarationList.get(rootNode.declarationList.size() - 1) : null;
        if (last == null ||
            last.type != DeclarationNode.Type.Function || 
            last.typeSpecifier.type != TypeSpecifierNode.Type.Void ||
            !last.identifier.equals("main") ||
            last.functionParameters.size() != 0) {

            throw new CompileErrorException("The last declaration in a program must be a function declaration of the form `void main(void)`");
        }
    }

    private static void check(SymbolTable symbolTable, DeclarationNode node) throws CompileErrorException {
        Entry entry = null;
        if(node.type == DeclarationNode.Type.Variable) {
                // Verify that type is int
                if (node.typeSpecifier.type == TypeSpecifierNode.Type.Void) {
                    throw new CompileErrorException("The variable, '" + node.identifier + "', cannot be declared with a void type", node.typeSpecifier.getLocation());
                }
                entry = new VariableEntry(node.getLocation(), node.identifier);
                symbolTable.insert(node.identifier, entry);
        } else if(node.type == DeclarationNode.Type.ArrayVariable) {
                // Verify that base type is int
                if (node.typeSpecifier.type == TypeSpecifierNode.Type.Void) {
                    throw new CompileErrorException("The array variable, '" + node.identifier + "', cannot be declared with a void type", node.typeSpecifier.getLocation());
                }
                // Verify that integer size is >0 (we have no negative numbers)
                if (node.arraySize == 0) {
                    throw new CompileErrorException("The array size cannot be zero", node.getLocation());
                }
                entry = new ArrayVariableEntry(node.getLocation(), node.identifier, node.arraySize);
                symbolTable.insert(node.identifier, entry);
        } else if(node.type == DeclarationNode.Type.Function) {
                // Verify that all code paths of body return if the return type is not int
                ReturnType returnType = node.typeSpecifier.type == TypeSpecifierNode.Type.Int ? 
                    ReturnType.Int : 
                    ReturnType.Void;
                List<FunctionEntry.Parameter> parameters = new ArrayList<FunctionEntry.Parameter>();
                for(ParameterDeclarationNode param : node.functionParameters) {
                    parameters.add(new FunctionEntry.Parameter(param.identifier, param.isArray));
                }
                entry = new FunctionEntry(node.getLocation(), node.identifier, returnType, parameters, node.functionBody);
                symbolTable.insert(node.identifier, entry);

                boolean allPathsReturn = check(symbolTable, node.functionBody, node);
                
                if (node.typeSpecifier.type == TypeSpecifierNode.Type.Int && !allPathsReturn) {
                    throw new CompileErrorException("All paths in '" + node.identifier + "' must return an integer", node.getLocation());
                }
        }
    }

    private static boolean check(SymbolTable symbolTable, StatementNode node, DeclarationNode function) throws CompileErrorException {
        if(node.statementType() == StatementNode.Type.Compound) {
            return check(symbolTable, (CompoundStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Expression) {
            return check(symbolTable, (ExpressionStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Iteration) {
            return check(symbolTable, (IterationStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Read) {
            return check(symbolTable, (ReadStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Return) {
            return check(symbolTable, (ReturnStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Selection) {
            return check(symbolTable, (SelectionStatementNode)node, function);
        } else if(node.statementType() == StatementNode.Type.Write) {
            return check(symbolTable, (WriteStatementNode)node, function);
        } else {
            throw new CompileErrorException("Unrecognized statement type");
        }
    }

    private static boolean check(SymbolTable symbolTable, CompoundStatementNode node, DeclarationNode function) throws CompileErrorException {
        // Create a new scope for this block
        symbolTable = new SymbolTable(symbolTable);
        node.symbolTable = symbolTable;
        // Add the parameters to this scope if it is the function body of the current function
        if(node == function.functionBody) {
            for(ParameterDeclarationNode parameter: function.functionParameters) {
                symbolTable.insert(parameter.identifier, parameter.isArray ? 
                    new ArrayVariableEntry(parameter.getLocation(), parameter.identifier, null) : 
                    new VariableEntry(parameter.getLocation(), parameter.identifier));
            }
        }
        
        for(DeclarationNode declaration: node.localDeclarations) {
            check(symbolTable, declaration);
        }

        boolean allPathsReturned = false;
        for(StatementNode statement: node.statements) {
            if(check(symbolTable, statement, function)) {
                allPathsReturned = true;
            }
        }
        return allPathsReturned;
    }
    
    private static boolean check(SymbolTable symbolTable, ExpressionStatementNode node, DeclarationNode function) throws CompileErrorException {
        check(symbolTable, node.expression, function);
        return false; // No Return
    }
    
    private static boolean check(SymbolTable symbolTable, IterationStatementNode node, DeclarationNode function) throws CompileErrorException {
        // Verify condition is not void
        if(check(symbolTable, node.condition, function) != ResultType.Integer) {
            throw new CompileErrorException("The condition for a while loop must be an integer", node.condition.getLocation());
        }
        check(symbolTable, node.block, function);
        return false; // No Return
    }
    
    private static boolean check(SymbolTable symbolTable, ReadStatementNode node, DeclarationNode function) throws CompileErrorException {
        if(check(symbolTable, node.reference, function) != ResultType.Integer) {
            throw new CompileErrorException("The variable for a read statement must accept assignment of an integer", node.getLocation());
        }
        return false; // No Return
    }
    
    private static boolean check(SymbolTable symbolTable, ReturnStatementNode node, DeclarationNode function) throws CompileErrorException {
        if(function.typeSpecifier.type == TypeSpecifierNode.Type.Int) {
            // Verify that values are returned for int functions
            if(node.expression == null || check(symbolTable, node.expression, function) != ResultType.Integer) {
                throw new CompileErrorException("All return statements in '" + function.identifier + "' must return integers", node.getLocation());
            }
        } else {
            // Verify that no value is returned for void functions
            if(node.expression != null) {
                throw new CompileErrorException("Return statements in '" + function.identifier + "' cannot return values", node.expression.getLocation());
            }
        }
        return true; // Returns
    }
    
    private static boolean check(SymbolTable symbolTable, SelectionStatementNode node, DeclarationNode function) throws CompileErrorException {
        // Verify condition is not void
        if(check(symbolTable, node.condition, function) != ResultType.Integer) {
            throw new CompileErrorException("The condition for a if statement must be an integer", node.condition.getLocation());
        }
        boolean ifReturns = check(symbolTable, node.ifBlock, function);
        boolean elseReturns = node.elseBlock != null && check(symbolTable, node.elseBlock, function);
        
        // Only true if both the if and the else are present
		return ifReturns && elseReturns;
    }
    
    private static boolean check(SymbolTable symbolTable, WriteStatementNode node, DeclarationNode function) throws CompileErrorException {
        if(check(symbolTable, node.expression, function) != ResultType.Integer) {
            throw new CompileErrorException("Only integers can be used with 'write'", node.getLocation());
        }
        return false; // No Return
    }

    private static ResultType check(SymbolTable symbolTable, ExpressionNode node, DeclarationNode function) throws CompileErrorException {
        if(node.expressionType() == ExpressionNode.Type.Assignment) {
            return check(symbolTable, (AssignmentExpressionNode)node, function);
        } else if(node.expressionType() == ExpressionNode.Type.Binary) {
            return check(symbolTable, (BinaryExpressionNode)node, function);
        } else if(node.expressionType() == ExpressionNode.Type.Call) {
            return check(symbolTable, (CallExpressionNode)node, function);
        } else if(node.expressionType() == ExpressionNode.Type.Literal) {
            return check(symbolTable, (LiteralExpressionNode)node, function);
        } else if(node.expressionType() == ExpressionNode.Type.Nested) {
            return check(symbolTable, (NestedExpressionNode)node, function);
        } else if(node.expressionType() == ExpressionNode.Type.Variable) {
            return check(symbolTable, (VariableExpressionNode)node, function);
        } else {
            throw new CompileErrorException("Unrecognized statement type");
        }
    }

    private static ResultType check(SymbolTable symbolTable, AssignmentExpressionNode node, DeclarationNode function) throws CompileErrorException {
        // Verify that right expression is int
        if(check(symbolTable, node.expression, function) != ResultType.Integer) {
            throw new CompileErrorException("Only integers can be assigned to variables", node.expression.getLocation());
        }
        if(check(symbolTable, node.variable, function) != ResultType.Integer) {
            throw new CompileErrorException("Only simple variables or subscripted arrays variables can be the left hand of an expression", node.variable.getLocation());
        }
        return ResultType.Integer;
    }

    private static ResultType check(SymbolTable symbolTable, BinaryExpressionNode node, DeclarationNode function) throws CompileErrorException {
        if(check(symbolTable, node.left, function) != ResultType.Integer || check(symbolTable, node.right, function) != ResultType.Integer) {
            throw new CompileErrorException("Both sides of an " + node.operation.type.toString().toLowerCase() + " expression must result in integers", node.getLocation());
        }
        return ResultType.Integer;
    }

    private static ResultType check(SymbolTable symbolTable, CallExpressionNode node, DeclarationNode function) throws CompileErrorException {
        Entry entry = symbolTable.get(node.identifier, node.getLocation());

        if(entry.getType() != Entry.Type.Function) {
            throw new CompileErrorException("Cannot invoke the variable, '" + entry.identifier + "' as a function", node.getLocation());
        }

        FunctionEntry functionDeclaration = (FunctionEntry)entry;
        if(functionDeclaration.parameters.size() != node.arguments.size()) {
            throw new CompileErrorException("Function, '" + entry.identifier + "' received " +node.arguments.size() + " arguments, but " + functionDeclaration.parameters.size() + " were expected", node.getLocation());
        }

        for(int x = 0; x < node.arguments.size(); x ++) {
            FunctionEntry.Parameter declared = functionDeclaration.parameters.get(x);
            ExpressionNode argument = node.arguments.get(x);
            if (check(symbolTable, argument, function) == ResultType.Void) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' received an expression that evaluated to void", argument.getLocation());
            }
            if(declared.isArray && check(symbolTable, argument, function) == ResultType.Integer) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' received an expression that evaluated to an integer, but an integer array was expected", argument.getLocation());
            }
            if(!declared.isArray && check(symbolTable, argument, function) == ResultType.IntegerArray) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' received an expression that evaluated to an integer array, but an integer was expected", argument.getLocation());
            }
            if(declared.isArray && argument.expressionType() != ExpressionNode.Type.Variable) {
                throw new CompileErrorException("Argument " + (x + 1) + " to function, '" + entry.identifier + "' requires a variable reference, not an expression", argument.getLocation());
            }
        }

        return functionDeclaration.returnType == ReturnType.Int ? ResultType.Integer : ResultType.Void;
    }

    private static ResultType check(SymbolTable symbolTable, LiteralExpressionNode node, DeclarationNode function) throws CompileErrorException {
        return ResultType.Integer;
    }

    private static ResultType check(SymbolTable symbolTable, NestedExpressionNode node, DeclarationNode function) throws CompileErrorException {
        return check(symbolTable, node.expression, function);
    }

    private static ResultType check(SymbolTable symbolTable, VariableExpressionNode node, DeclarationNode function) throws CompileErrorException {
        Entry entry = symbolTable.get(node.identifier, node.getLocation());
        if(entry.getType() == Entry.Type.Function) {
            throw new CompileErrorException("Cannot use function, '" + entry.identifier +"', as a variable", node.getLocation());
        }
        
        if (entry.getType() == Entry.Type.ArrayVariable) {
            if(node.arrayExpression != null && check(symbolTable, node.arrayExpression, function) != ResultType.Integer) {
                // Verify the expression for index results in an integer
                throw new CompileErrorException("Array variables must be indexed by integers", node.arrayExpression.getLocation());
            }
            return node.arrayExpression == null ? ResultType.IntegerArray : ResultType.Integer;
        } else {
            if(node.arrayExpression != null) {
                throw new CompileErrorException("Array notation (i.e., d[x]) can only be used for array variables", node.getLocation());
            }
            return ResultType.Integer;
        }
    }

    private enum ResultType {
        Integer,
        IntegerArray,
        Void
    }
}