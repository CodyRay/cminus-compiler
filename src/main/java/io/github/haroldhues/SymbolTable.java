package io.github.haroldhues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.haroldhues.SyntaxTree.CompoundStatementNode;
import io.github.haroldhues.SyntaxTree.ExpressionNode;
import io.github.haroldhues.SyntaxTree.StatementNode;
import io.github.haroldhues.SyntaxTree.VariableExpressionNode;

public class SymbolTable {
    private SymbolTable outerScope;
    private Map<String, Entry> lookup = new HashMap<String, Entry>();
    public SymbolTable(SymbolTable outerScope) {
        this.outerScope = outerScope;
    }

    private SymbolTable() {}

    protected void recordSymbol(Entry entry) {
        getOuterScope().recordSymbol(entry);
    }

    public int getLevel() {
        return outerScope.getLevel() + 1;
    }

    public String getUniqueName() {
        return outerScope.getUniqueName();
    }

    public SymbolTable getOuterScope() {
        return outerScope;
    }

    public String getRecordedSymbols() {
        return outerScope.getRecordedSymbols();
    }

    public static SymbolTable newGlobal() {
        return new GlobalSymbolTable();
    }

    public void insert(String identifier, Entry entry) throws CompileErrorException {
        if(lookup.containsKey(identifier)) {
            throw new CompileErrorException("'" + identifier + "' has already been declared in this scope", entry.getLocation());
        }
        entry.setRename(getUniqueName());
        entry.setLevel(getLevel());
        lookup.put(identifier, entry);
        recordSymbol(entry);
    }

    public Entry get(String identifier) throws CompileErrorException {
        return get(identifier, Location.None);
    }

    public Entry get(String identifier, Location location) throws CompileErrorException {
        return lookup.containsKey(identifier) ? lookup.get(identifier) : outerScope.get(identifier, location);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        lookup.forEach((identifier, entry) -> {
            builder.append(identifier);
            builder.append(": ");
            builder.append(entry);
            builder.append('\n');
        });
        if(outerScope != null) {
            builder.append("[\n");
            for(String line: outerScope.toString().split("\n")) {
                builder.append("     ");
                builder.append(line);
                builder.append('\n');
            }
            builder.append("]");
        }

        return builder.toString();
    }

    public static abstract class Entry {
        public enum Type {
            Variable,
            ArrayVariable,
            Function,
        }

        public Entry(Location location) {
            this.location = location;
        }

        public abstract void initialize();

        public Location getLocation() {
            return location;
        }

        public void setRename(String rename) {
            this.rename = rename;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String identifier;
        private Location location;
        public String rename;
        public Integer level;

        public abstract Type getType();
    }

    public static class FunctionEntry extends Entry {
        public ReturnType returnType;
        public List<Parameter> parameters;
        public CompoundStatementNode body;

        public FunctionEntry(Location location, String identifier, ReturnType returnType, List<Parameter> parameters, CompoundStatementNode body) {
            super(location);
            this.identifier = identifier;
            this.returnType = returnType;
            this.parameters = parameters;
            this.body = body;
        }

        public void initialize() {
            // Nothing to do because a function has no state
        }

        public Type getType() {
            return Type.Function;
        }

        public enum ReturnType {
            Void,
            Int
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append("identifier: ");
            builder.append(identifier);
            builder.append(", type: ");
            builder.append(getType());
            builder.append(", location: ");
            builder.append(getLocation());
            builder.append(", level: ");
            builder.append(level);
            builder.append(", rename: ");
            builder.append(rename);
            builder.append(", returnType: ");
            builder.append(returnType);
            builder.append(", parameters: ");
            builder.append(parameters);
            builder.append(" }");
            return builder.toString();
        }

        public static class Parameter {
            public boolean isArray;
            public String identifier;

            public Parameter(String identifier, boolean isArray) {
                this.identifier = identifier;
                this.isArray = isArray;
            }

            public String toString() {
                StringBuilder builder = new StringBuilder();
                builder.append("{ ");
                builder.append("identifier: ");
                builder.append(identifier);
                builder.append(", isArray: ");
                builder.append(isArray);
                builder.append(" }");
                return builder.toString();
            }
        }
    }

    public static class VariableEntry extends Entry {
        private int value;
        public Type getType() {
            return Type.Variable;
        }

        public VariableEntry(Location location, String identifier) {
            super(location);
            this.identifier = identifier;
        }

        public void initialize() {
            value = 0;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append("identifier: ");
            builder.append(identifier);
            builder.append(", type: ");
            builder.append(getType());
            builder.append(", location: ");
            builder.append(getLocation());
            builder.append(", level: ");
            builder.append(level);
            builder.append(", rename: ");
            builder.append(rename);
            builder.append(" }");
            return builder.toString();
        }
    }

    public static class ArrayVariableEntry extends Entry {
        public Integer size = null;
        public ArrayVariableEntry reference = null;
        public int[] values;

        public ArrayVariableEntry(Location location, String identifier, Integer size) {
            super(location);
            this.identifier = identifier;
            this.size = size;
        }

        public void initialize() {
            if(size != null) {
                values = new int[size]; // Ints will be initialized to 0;
            }
        }

        public void setValue(ArrayVariableEntry reference) {
            this.reference = reference;
        }

        public void setValue(int index, int value) throws CompileErrorException {
            if(size != null) {
                if(index < 0 || index >= size) {
                    values[index] = value;
                } else {
                    throw new CompileErrorException("Index of " + index + " out of bounds");
                }
            } else {
                reference.setValue(index, value);
            }
        }

        public int getValue(int index) throws CompileErrorException {
            if(size != null) {
                if(index < 0 || index >= size) {
                    return values[index];
                } else {
                    throw new CompileErrorException("Index of " + index + " out of bounds");
                }
            } else {
                return reference.getValue(index);
            }
        }

        public Type getType() {
            return Type.ArrayVariable;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append("identifier: ");
            builder.append(identifier);
            builder.append(", type: ");
            builder.append(getType());
            builder.append(", location: ");
            builder.append(getLocation());
            builder.append(", level: ");
            builder.append(level);
            builder.append(", rename: ");
            builder.append(rename);
            builder.append(", size: ");
            builder.append(size);
            builder.append(" }");
            return builder.toString();
        }
    }


    private static class GlobalSymbolTable extends SymbolTable {
        private int counter = 0;
        private List<Entry> recorded = new ArrayList<Entry>();

        public int getLevel() {
            return -1;
        }

        public String getUniqueName() {
            counter++;
            return getFancyString(counter);
        }

        // http://codereview.stackexchange.com/a/44549
        private static String getFancyString(int number) {
            StringBuilder builder = new StringBuilder();
            while (number-- > 0) {
                builder.append((char)('a' + (number % 26)));
                number /= 26;
            }
            return builder.reverse().toString();
        }

        public void insert(String identifier, Entry entry) throws CompileErrorException {
            throw new UnsupportedOperationException("Cannot insert into global symbol table");
        }

        public Entry get(String identifier, Location location) throws CompileErrorException {
            throw new CompileErrorException("'" + identifier + "' has not been defined", location);
        }

        protected void recordSymbol(Entry entry) {
            recorded.add(entry);
        }

        public String getRecordedSymbols() {
            StringBuilder builder = new StringBuilder();
            for(Entry record: recorded) {
                builder.append(record);
                builder.append('\n');
            }
            return builder.toString();
        }
    }
}