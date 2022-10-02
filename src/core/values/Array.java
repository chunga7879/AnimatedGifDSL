package core.values;

import java.util.ArrayList;

public class Array extends Value{
    public static final String NAME = "Array";

    private final ArrayList<Value> arr;

    public Array() {
        super(Array.NAME);
        this.arr = new ArrayList<>();
    }

    public void add(Value v) {
       this.arr.add(v);
    }

    public ArrayList<Value> get() {
        return this.arr;
    }

    @Override
    public Array asArray() {
        return this;
    }
}
