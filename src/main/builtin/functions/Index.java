package builtin.functions;

import core.Scope;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class Index extends AbstractFunction {
    private final static String ACTUAL_NAME = "Index";

    @Override
    public Value call(Scope scope) {
        return scope.getLocalVar("a").asArray().get().get(scope.getLocalVar("i").asInteger().get());
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Value checkReturn() {
        return new Image(null);
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("a", Array.NAME);
            put("i", IntegerValue.NAME);
        }};
    }
}
