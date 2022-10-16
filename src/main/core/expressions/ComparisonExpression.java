package core.expressions;

import core.expressions.comparison.ComparisonVisitor;

public final class ComparisonExpression extends Expression {
    private final Expression a;
    private final Expression b;
    private final ComparisonVisitor cv;

    public ComparisonExpression(Expression a, Expression b, ComparisonVisitor cv) {
        this.a = a;
        this.b = b;
        this.cv = cv;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public Expression a() {
        return a;
    }

    public Expression b() {
        return b;
    }

    public ComparisonVisitor cv() {
        return cv;
    }
}
