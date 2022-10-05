package core.values;

import java.util.ArrayList;
import java.util.Iterator;

public class Array extends Value implements Iterable<Value> {
    public static final String NAME = "Array";

    private final ArrayList<Value> a;

    public Array() {
        super(Array.NAME);
        this.a = new ArrayList<>();
    }

    public void add(Value v) {
       this.a.add(v);
    }

    @Override
    public Array asArray() {
        return this;
    }

    public Iterator<Value> iterator() {
        return a.iterator();
    }
}
