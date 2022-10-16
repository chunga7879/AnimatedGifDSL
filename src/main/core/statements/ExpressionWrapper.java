package core.statements;

import core.expressions.Expression;

public final class ExpressionWrapper extends Statement {
    private final Expression e;

    public ExpressionWrapper(Expression e) {
        this.e = e;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public Expression e() {
        return e;
    }
}
