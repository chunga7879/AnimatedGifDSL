package core.expressions;

import core.expressions.arithmetic.ArithmeticVisitor;

public final class ArithmeticExpression extends Expression {
    private final Expression a;
    private final Expression b;
    private final ArithmeticVisitor av;

    public ArithmeticExpression(Expression a, Expression b, ArithmeticVisitor av) {
        this.a = a;
        this.b = b;
        this.av = av;
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

    public ArithmeticVisitor av() {
        return av;
    }
}
