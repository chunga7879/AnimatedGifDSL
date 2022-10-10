package core.values;

import core.expressions.ExpressionVisitor;

public class Null extends Value {
    public static final Null NULL = new Null();
    private static final String NAME = "Null";

    public Null() {
        super(Null.NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
