package builtin.functions;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class Add extends AbstractFunction {
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
            put("item", IntegerValue.NAME);
            // TODO
            //put("array", IntegerValue.NAME);
        }};
        checker(scope, params);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
