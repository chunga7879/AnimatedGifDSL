package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Null;
import core.values.StringValue;

import java.util.HashMap;
import java.util.Map;

public class Print extends AbstractFunction {
    public final static String ACTUAL_NAME = "Print";

    @Override
    public Null call(Scope scope) {
        System.out.println("builtin print: " + scope.getVar("msg").asString().get());
        return Null.NULL;
    }

    @Override
    public Null checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("msg", StringValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return Null.NULL;
    }
}
