package core.values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Array extends Value implements Iterable<Value> {
    public static final String NAME = "Array";

    private final ArrayList<Value> arr;

    public Array() {
        super(Array.NAME);
        this.arr = new ArrayList<>();
    }

    public Array(List<Value> array) {
        this();
        this.arr.addAll(array);
    }

    /**
     * Add and return copy of array
     */
    public Array addCopy(Value v) {
        Array newArray = new Array(this.arr);
        newArray.arr.add(v);
        return newArray;
    }

    public ArrayList<Value> get() {
        return this.arr;
    }

    @Override
    public Array asArray() {
        return this;
    }

    public Iterator<Value> iterator() {
        return arr.iterator();
    }
}
