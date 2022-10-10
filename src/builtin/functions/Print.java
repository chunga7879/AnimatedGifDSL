package builtin.functions;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class Print extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        System.out.println("builtin print: " + scope.getVar("msg").asString().get());
        return Null.NULL;
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("msg", StringValue.NAME);
        }};
        checker(scope, params);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
