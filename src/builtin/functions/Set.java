package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Unknown;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class Set extends AbstractFunction {
    public final static String ACTUAL_NAME = "Set";

    @Override
    public Value call(Scope scope) {
        return scope.getVar(AbstractFunction.PARAM_TARGET);
    }

    @Override
    public Value checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Unknown.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new Unknown();
    }
}
