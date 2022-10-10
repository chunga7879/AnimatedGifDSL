package core.expressions;

import core.Scope;
import core.values.Value;

public interface Expression {
    Value evaluate(Scope s);

    <C, T> T accept(C ctx, ExpressionVisitor<C, T> v);
}
