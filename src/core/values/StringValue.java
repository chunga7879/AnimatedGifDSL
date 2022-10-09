package core.values;

import core.Scope;
import core.expressions.Expression;
import core.expressions.ExpressionVisitor;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.expressions.comparison.ComparisonVisitor;

public class StringValue extends Value {
    public static final String NAME = "string";

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
    public BooleanValue acceptCV(ComparisonVisitor cv, Expression b, Scope s) {
        return cv.visit(this, b, s);
    }

    @Override
    public Value acceptAV(ArithmeticVisitor av, Expression b, Scope s) {
        return av.visit(this, b, s);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
