package io.github.haroldhues.SyntaxTree;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import io.github.haroldhues.Location;





public abstract class SyntaxTreeNode 
{
    private Location location = null;

    public SyntaxTreeNode(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
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