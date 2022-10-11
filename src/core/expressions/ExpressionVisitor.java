package core.expressions;

import core.values.AbstractFunction;
import core.values.Function;
import core.values.Value;

public interface ExpressionVisitor<C, T> {
    T visit(C ctx, ArithmeticExpression ae);

    T visit(C ctx, ComparisonExpression ce);

    T visit(C ctx, FunctionCall fc);

    T visit(C ctx, VariableExpression ve);

    T visit(C ctx, Value v);

    T visit(C ctx, AbstractFunction f);

    T visit(C ctx, Function f);
}
