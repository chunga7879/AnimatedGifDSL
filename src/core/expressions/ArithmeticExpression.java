package core.expressions;

import core.expressions.arithmetic.ArithmeticVisitor;

public record ArithmeticExpression(Expression a, Expression b, ArithmeticVisitor av) implements Expression {

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
