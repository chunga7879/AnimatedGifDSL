package core.expressions;

import builtin.functions.*;
import core.values.*;

public interface ExpressionVisitor<C, T> {
    T visit(C ctx, ArithmeticExpression ae);
    T visit(C ctx, ComparisonExpression ce);
    T visit(C ctx, FunctionCall fc);
    T visit(C ctx, VariableExpression ve);

    T visit(C ctx, Array a);
    T visit(C ctx, BooleanValue bv);
    T visit(C ctx, Colour c);
    T visit(C ctx, Function f);
    T visit(C ctx, Image i);
    T visit(C ctx, IntegerValue iv);
    T visit(C ctx, Null n);
    T visit(C ctx, StringValue sv);

    T visit(C ctx, Add a);
    T visit(C ctx, CreateList cl);
    T visit(C ctx, Load l);
    T visit(C ctx, Print p);
    T visit(C ctx, Random r);
    T visit(C ctx, Save s);
}
