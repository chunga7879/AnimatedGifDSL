package core.statements;

import core.expressions.Expression;

public record ExpressionWrapper(Expression e) implements Statement {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
