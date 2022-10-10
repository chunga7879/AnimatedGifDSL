package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class Add extends AbstractFunction {
    public final static String ACTUAL_NAME = "Add";

    @Override
    public Value call(Scope scope) {
        Value i = scope.getVar("item");
        Array a = scope.getVar("array").asArray();

        a.add(i);

        return Null.NULL;
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("array", Array.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
