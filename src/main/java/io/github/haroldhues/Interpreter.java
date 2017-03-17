package io.github.haroldhues;

import io.github.haroldhues.SymbolTable.*;

import io.github.haroldhues.SyntaxTree.*;

public class Interpreter {
    public static void run(RootNode rootNode, InputOutput io) throws CompileErrorException {
        SymbolTable symbolTable = rootNode.symbolTable;
        FunctionEntry main = (FunctionEntry)symbolTable.get("main");

        for(DeclarationNode declaration: rootNode.declarationList) {
            Entry entry = symbolTable.get(declaration.identifier);
            entry.initialize();
        }

        run(symbolTable, main.body, io);
    }

    private static Integer run(SymbolTable symbolTable, StatementNode node, InputOutput io) throws CompileErrorException {
        if(node.statementType() == StatementNode.Type.Compound) {
            return run(symbolTable, (CompoundStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Expression) {
            return run(symbolTable, (ExpressionStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Iteration) {
            return run(symbolTable, (IterationStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Read) {
            return run(symbolTable, (ReadStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Return) {
            return run(symbolTable, (ReturnStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Selection) {
            return run(symbolTable, (SelectionStatementNode)node, io);
        } else if(node.statementType() == StatementNode.Type.Write) {
            return run(symbolTable, (WriteStatementNode)node, io);
        } else {
            throw new CompileErrorException("Unrecognized statement type");
        }
    }

    private static Integer run(SymbolTable symbolTable, CompoundStatementNode node, InputOutput io) throws CompileErrorException {
        // Create a new scope for this block
        symbolTable = node.symbolTable;

        for(DeclarationNode declaration: node.localDeclarations) {
            Entry entry = symbolTable.get(declaration.identifier);
            entry.initialize();
        }
        
        for(StatementNode statement: node.statements) {
            Integer result = run(symbolTable, statement, io);
            if(result != null) {
                return result;
            }
        }
        return null;
    }
    
    private static Integer run(SymbolTable symbolTable, ExpressionStatementNode node, InputOutput io) throws CompileErrorException {
        run(symbolTable, node.expression, io);
        return null; // No Return
    }
    
    private static Integer run(SymbolTable symbolTable, IterationStatementNode node, InputOutput io) throws CompileErrorException {
        while(run(symbolTable, node.condition, io) > 0) {
            Integer result = run(symbolTable, node.block, io);
            if(result != null) {
                return result;
            }
        }
        return null; // No Return
    }
    
    private static Integer run(SymbolTable symbolTable, ReadStatementNode node, InputOutput io) throws CompileErrorException {
        int in = io.read();
        assign(symbolTable, io, node.reference, in);
        return null; // No Return
    }
    
    private static Integer run(SymbolTable symbolTable, ReturnStatementNode node, InputOutput io) throws CompileErrorException {
        return run(symbolTable, node.expression, io); // Returns
    }
    
    private static Integer run(SymbolTable symbolTable, SelectionStatementNode node, InputOutput io) throws CompileErrorException {
        if(run(symbolTable, node.condition, io) > 0) {
            return run(symbolTable, node.ifBlock, io);
        } else {
            return run(symbolTable, node.elseBlock, io);
        }
    }
    
    private static Integer run(SymbolTable symbolTable, WriteStatementNode node, InputOutput io) throws CompileErrorException {
        io.write(run(symbolTable, node.expression, io));
        return null; // No Return
    }

    private static Integer run(SymbolTable symbolTable, ExpressionNode node, InputOutput io) throws CompileErrorException {
        if(node.expressionType() == ExpressionNode.Type.Assignment) {
            return run(symbolTable, (AssignmentExpressionNode)node, io);
        } else if(node.expressionType() == ExpressionNode.Type.Binary) {
            return run(symbolTable, (BinaryExpressionNode)node, io);
        } else if(node.expressionType() == ExpressionNode.Type.Call) {
            return run(symbolTable, (CallExpressionNode)node, io);
        } else if(node.expressionType() == ExpressionNode.Type.Literal) {
            return run(symbolTable, (LiteralExpressionNode)node, io);
        } else if(node.expressionType() == ExpressionNode.Type.Nested) {
            return run(symbolTable, (NestedExpressionNode)node, io);
        } else if(node.expressionType() == ExpressionNode.Type.Variable) {
            return run(symbolTable, (VariableExpressionNode)node, io);
        } else {
            throw new CompileErrorException("Unrecognized statement type");
        }
    }

    private static Integer run(SymbolTable symbolTable, AssignmentExpressionNode node, InputOutput io) throws CompileErrorException {
        Integer value = run(symbolTable, node.expression, io);
        assign(symbolTable, io, node.variable, value);
        return value;
    }

    private static Integer run(SymbolTable symbolTable, BinaryExpressionNode node, InputOutput io) throws CompileErrorException {
        switch(node.operation.type) {
            case Add:
                return run(symbolTable, node.left, io) + run(symbolTable, node.right, io);
            case Subtract:
                return run(symbolTable, node.left, io) - run(symbolTable, node.right, io);
            case Multiply:
                return run(symbolTable, node.left, io) * run(symbolTable, node.right, io);
            case Divide:
                return run(symbolTable, node.left, io) / run(symbolTable, node.right, io);
            case NotEqual:
                return run(symbolTable, node.left, io) != run(symbolTable, node.right, io) ? 1 : 0;
            case Equal:
                return run(symbolTable, node.left, io) == run(symbolTable, node.right, io) ? 1 : 0;
            case LessThan:
                return run(symbolTable, node.left, io) < run(symbolTable, node.right, io) ? 1 : 0;
            case LessThanOrEqual:
                return run(symbolTable, node.left, io) <= run(symbolTable, node.right, io) ? 1 : 0;
            case GreaterThan:
                return run(symbolTable, node.left, io) > run(symbolTable, node.right, io) ? 1 : 0;
            case GreaterThanOrEqual:
                return run(symbolTable, node.left, io) >= run(symbolTable, node.right, io) ? 1 : 0;
            default:
                throw new UnsupportedOperationException("Unsupported Operation");
        }
    }

    private static Integer run(SymbolTable symbolTable, CallExpressionNode node, InputOutput io) throws CompileErrorException {
        FunctionEntry entry = (FunctionEntry) symbolTable.get(node.identifier);
        for(int x = 0; x < node.arguments.size(); x++) {
            FunctionEntry.Parameter param = entry.parameters.get(x);
            ExpressionNode arg = node.arguments.get(x);
            if(param.isArray) {
                ArrayVariableEntry variable = (ArrayVariableEntry)entry.body.symbolTable.get(param.identifier);
                VariableExpressionNode reference = (VariableExpressionNode)arg;
                variable.setValue((ArrayVariableEntry)symbolTable.get(reference.identifier));
            } else {
                VariableEntry variable = (VariableEntry)entry.body.symbolTable.get(param.identifier);
                variable.setValue(run(symbolTable, node.arguments.get(x), io));
            }
        }

        return run(symbolTable, entry.body, io);
    }

    private static Integer run(SymbolTable symbolTable, LiteralExpressionNode node, InputOutput io) throws CompileErrorException {
        return node.value;
    }

    private static Integer run(SymbolTable symbolTable, NestedExpressionNode node, InputOutput io) throws CompileErrorException {
        return run(symbolTable, node.expression, io);
    }

    private static Integer run(SymbolTable symbolTable, VariableExpressionNode node, InputOutput io) throws CompileErrorException {
        return value(symbolTable, io, node);
    }

    private static void assign(SymbolTable symbolTable, InputOutput io, VariableExpressionNode variable, int value) throws CompileErrorException {
        if(variable.arrayExpression != null) {
            ArrayVariableEntry entry = (ArrayVariableEntry)symbolTable.get(variable.identifier);
            entry.setValue(run(symbolTable, variable.arrayExpression, io), value);
        } else {
            VariableEntry entry = (VariableEntry)symbolTable.get(variable.identifier);
            entry.setValue(value);
        }
    }

    private static int value(SymbolTable symbolTable, InputOutput io, VariableExpressionNode variable) throws CompileErrorException {
        if(variable.arrayExpression != null) {
            ArrayVariableEntry entry = (ArrayVariableEntry)symbolTable.get(variable.identifier);
            return entry.getValue(run(symbolTable, variable.arrayExpression, io));
        } else {
            VariableEntry entry = (VariableEntry)symbolTable.get(variable.identifier);
            return entry.getValue();
        }
    }
}