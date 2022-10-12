package core.expressions;

import core.expressions.comparison.ComparisonVisitor;

public record ComparisonExpression(Expression a, Expression b, ComparisonVisitor cv) implements Expression {

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
