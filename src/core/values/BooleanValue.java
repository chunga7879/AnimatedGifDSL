package core.values;

import core.expressions.ExpressionVisitor;

public class BooleanValue extends Value {
    public final static String NAME = "Boolean";

    private final boolean b;

    public BooleanValue(boolean b) {
        super(BooleanValue.NAME);
        this.b=b;
    }

    public boolean get() {
        return this.b;
    }

    @Override
    public BooleanValue asBoolean() {
        return this;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
