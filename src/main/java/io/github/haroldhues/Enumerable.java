package io.github.haroldhues;
import java.util.List;
import java.util.ArrayList;

// Note: similar to java.util.Iterator<E>
public abstract class Enumerable<T extends Object>
{
    public List<T> toList() throws CompileErrorException {
        List<T> items = new ArrayList<T>();
        T n = next();
        while(n != null) {
            items.add(n);
            n = next();
        }
        return items;
    }

    public abstract T next() throws CompileErrorException;
}
