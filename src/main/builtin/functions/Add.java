package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Unknown;
import core.values.Value;

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
    public String getFunctionName() {
        return ACTUAL_NAME;
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
