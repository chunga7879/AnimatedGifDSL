package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class Add extends AbstractFunction {
    public final static String ACTUAL_NAME = "Add";

    @Override
    public Array call(Scope scope) {
        Array a = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asArray();
        Value i = scope.getLocalVar("item");

        return a.addCopy(i);
    }

    @Override
    public Array checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);

        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Array.NAME);
            put("item", Unknown.NAME);
        }};
    }

    @Override
    public Array checkReturn() {
        return new Array();
    }
}
