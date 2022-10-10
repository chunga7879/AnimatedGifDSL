package core.values;

import core.expressions.ExpressionVisitor;

import java.util.ArrayList;
import java.util.Iterator;

public class Array extends Value implements Iterable<Value> {
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

    public Iterator<Value> iterator() {
        return arr.iterator();
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
