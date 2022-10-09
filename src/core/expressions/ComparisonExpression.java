package core.expressions;

import core.Scope;
import core.expressions.comparison.ComparisonVisitor;
import core.values.BooleanValue;
import core.values.Value;

public class ComparisonExpression implements Expression {
    private final Expression a;
    private final Expression b;
    private final ComparisonVisitor cv;

    public ComparisonExpression(Expression a, Expression b, ComparisonVisitor cv) {
        this.a = a;
        this.b = b;
        this.cv = cv;
    }

    @Override
    public BooleanValue evaluate(Scope s) {
        return this.a.evaluate(s).acceptCV(this.cv, this.b, s);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
