package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import io.github.haroldhues.Location;





public abstract class SyntaxTreeNode 
{
    protected Location location = null;

    public SyntaxTreeNode(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
    
    public abstract String toAstString();

    public String buildAstString(String name, String... args) {
        StringBuilder builder = new StringBuilder();

        builder.append("new ");
        builder.append(name);
        builder.append("(");
        builder.append('\n');

        StringBuilder indentedBuilder = new StringBuilder();
        for(int x = 0; x < args.length; x++) {
            indentedBuilder.append(args[x]);
            if (x != args.length - 1) {
                indentedBuilder.append(", ");
            }
            indentedBuilder.append('\n');
        }
        for(String line: indentedBuilder.toString().split("\n")) {
            builder.append("    ");
            builder.append(line);
            builder.append('\n');
        }
        builder.append(')');

        return builder.toString();
    }

    public String buildAstList(String name, List<String> args) {
        StringBuilder builder = new StringBuilder();

        builder.append("new ArrayList<");
        builder.append(name);
        builder.append(">(Arrays.asList(");
        builder.append("\n");

        StringBuilder indentedBuilder = new StringBuilder();
        for(int x = 0; x < args.size(); x++) {
            indentedBuilder.append(args.get(x));
            if (x != args.size() - 1) {
                indentedBuilder.append(", ");
            }
            indentedBuilder.append('\n');
        }
        for(String line: indentedBuilder.toString().split("\n")) {
            builder.append("    ");
            builder.append(line);
            builder.append('\n');
        }
        builder.append("))");

        return builder.toString();
    }

    public static <T extends Object> EqualsBuilder<T> equalsBuilder(T tis) {
        return new EqualsBuilder<T>();
    }

    public static class EqualsBuilder<F> {
        private List<Function<F, Object>> properties = new ArrayList<Function<F, Object>>();

        public EqualsBuilder<F> property(Function<F, Object> property) {
            properties.add(property);
            return this;
        }

        public boolean result(F tis, Object other) {
            if (other == tis) {
                return true;
            }
            
            if (!tis.getClass().isInstance(other) || other == null) {
                return false;
            }

            @SuppressWarnings("unchecked") // Fine since, we checked at runtime
            F tat = (F) other;
    
            for(Function<F, Object> property : properties) {
                Object tisValue = property.apply(tis);
                Object tatValue = property.apply(tat);
                if ((tisValue == null) ? (tatValue != null) : !tisValue.equals(tatValue)) {
                	return false;
                }
            }
            return true;
        }
    }
}