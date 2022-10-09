package builtin.functions;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Value;

/**
 * Creates an empty list
 */
public class CreateList extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        return new Array();
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
