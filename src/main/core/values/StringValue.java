package core.values;

import core.Scope;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.expressions.comparison.ComparisonVisitor;

public class StringValue extends Value {
    public static final String NAME = "String";

    private final String s;

    public StringValue(String s) {
        super(StringValue.NAME);
        this.s = s;
    }

    public String get() {
        return this.s;
    }

    @Override
    public StringValue asString() {
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
