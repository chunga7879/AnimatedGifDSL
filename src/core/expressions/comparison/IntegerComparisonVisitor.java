package core.expressions.comparison;

import core.Scope;
import core.expressions.Expression;
import core.values.BooleanValue;
import core.values.IntegerValue;

public abstract class IntegerComparisonVisitor extends ComparisonVisitor {
    protected IntegerComparisonVisitor(String op) {
        super(op);
    }

    protected abstract boolean apply(int a, int b);

    @Override
    public BooleanValue visit(IntegerValue a, Expression b, Scope s) {
        return new BooleanValue(this.apply(a.get(), b.evaluate(s).asInteger().get()));
    }
}
