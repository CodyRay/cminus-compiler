package io.github.haroldhues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private SymbolTable outerScope;
    private Map<String, Entry> lookup = new HashMap<String, Entry>();
    public SymbolTable(SymbolTable outerScope) {
        this.outerScope = outerScope;
    }

    private SymbolTable() {}

    public SymbolTable getOuterScope() {
        return outerScope;
    }

    public static SymbolTable root() {
        return new RootSymbolTable();
    }

    public void insert(String identifier, Entry entry) throws CompileErrorException {
        if(lookup.containsKey(identifier)) {
            throw new CompileErrorException("'" + identifier + "' has already been declared in this scope");
        }
        lookup.put(identifier, entry);
    }


    public Entry get(String identifier) throws CompileErrorException {
        return lookup.containsKey(identifier) ? lookup.get(identifier) : outerScope.get(identifier);
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

        public String identifier;

        public abstract Type getType();
    }

    public static class FunctionEntry extends Entry {
        public ReturnType returnType;
        public List<Parameter> parameters;

        public FunctionEntry(String identifier, ReturnType returnType, List<Parameter> parameters) {
            this.identifier = identifier;
            this.returnType = returnType;
            this.parameters = parameters;
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
        public Type getType() {
            return Type.Variable;
        }

        public VariableEntry(String identifier) {
            this.identifier = identifier;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append("identifier: ");
            builder.append(identifier);
            builder.append(" }");
            return builder.toString();
        }
    }

    public static class ArrayVariableEntry extends Entry {
        public Integer size = null;

        public ArrayVariableEntry(String identifier, Integer size) {
            this.identifier = identifier;
            this.size = size;
        }

        public Type getType() {
            return Type.ArrayVariable;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ ");
            builder.append("identifier: ");
            builder.append(identifier);
            builder.append(", size: ");
            builder.append(size);
            builder.append(" }");
            return builder.toString();
        }
    }


    private static class RootSymbolTable extends SymbolTable {
        public void insert(String identifier, Entry entry) throws CompileErrorException {
            throw new UnsupportedOperationException("Cannot insert into root symbol table");
        }

        public Entry get(String identifier) throws CompileErrorException {
            throw new CompileErrorException("'" + identifier + "' has not been defined");
        }
    }
}