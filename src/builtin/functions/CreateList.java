package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates an empty list
 */
public class CreateList extends AbstractFunction {
    public final static String ACTUAL_NAME = "Create-List";

    @Override
    public Value call(Scope scope) {
        return new Array();
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>();
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
