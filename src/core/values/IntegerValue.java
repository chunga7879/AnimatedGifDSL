package core.values;

import core.Scope;
import core.expressions.Expression;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.expressions.comparison.ComparisonVisitor;

public class IntegerValue extends Value {
    public static final String NAME = "Integer";
    private final int n;

    public IntegerValue(int n) {
        super(IntegerValue.NAME);
        this.n = n;
    }

    public int get() {
        return this.n;
    }

    @Override
    public IntegerValue asInteger() {
        return this;
    }

    @Override
    public BooleanValue accept(ComparisonVisitor cv, Value b, Scope s) {
        return cv.visit(this, b, s);
    }

    @Override
    public Value accept(ArithmeticVisitor av, Value b, Scope s) {
        return av.visit(this, b, s);
    }
}
