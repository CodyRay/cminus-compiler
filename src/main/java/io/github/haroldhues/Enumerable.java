package io.github.haroldhues;
import java.util.List;
import java.util.ArrayList;

// Note: similar to java.util.Iterator<E>
public abstract class Enumerable<T>
{
    public enum Status {
        Value,
        Error,
        End
    }

    public class Item {
        private Status status;
        private T value;
        private CompileError error;

        public Item(T value) {
            status = Status.Value;
            this.value = value;
        }

        public Item(CompileError error) {
            status = Status.Error;
            this.error = error;
        }

        public Item() {
            status = Status.End;
        }

        public Status getStatus() {
            return status;
        }

        public T getValue() {
            if (status == Status.Value) {
                return value;
            }
            throw new UnsupportedOperationException("This item has no value");
        }

        public CompileError getError() {
            if (status == Status.Error) {
                return error;
            }
            throw new UnsupportedOperationException("This item has no error");
        }

    }

    public List<T> toList() throws Exception {
        List<T> items = new ArrayList<T>();
        Item n = next();
        while(n.getStatus() == Status.Value) {
            items.add(n.getValue());
            n = next();
        }
        if (n.getStatus() == Status.Error) {
            throw new Exception(n.getError().toString());
        }
        return items;
    }

    public abstract Item next();
}
