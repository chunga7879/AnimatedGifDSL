package core.expressions;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import core.values.*;

public interface ExpressionVisitor<C, T> {
    T visit(C ctx, ArithmeticExpression ae);

    T visit(C ctx, ComparisonExpression ce);

    T visit(C ctx, FunctionCall fc);

    T visit(C ctx, VariableExpression ve);

    T visit(C ctx, Value v);

    T visit(C ctx, AbstractFunction af);

    T visit(C ctx, Function f);
}
