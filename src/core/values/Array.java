package core.values;

import java.util.ArrayList;

public class Array extends Value{
    public static final String NAME = "Array";

    private final ArrayList<Value> a;

    public Array() {
        super(Array.NAME);
        this.a = new ArrayList<>();
    }

    public void add(Value v) {
       this.a.add(v);
    }
}
