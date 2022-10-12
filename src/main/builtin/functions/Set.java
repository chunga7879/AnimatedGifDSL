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
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return scope.getVar(AbstractFunction.PARAM_TARGET);
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Unknown.NAME);
        }};
    }

    @Override
    public Value checkReturn() {
        return new Unknown();
    }
}
