package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.StringValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class Concat extends AbstractFunction {
    public final static String ACTUAL_NAME = "Concat";

    @Override
    public Value call(Scope scope) {
        StringBuilder sb = new StringBuilder();
        for (Value v : scope.getLocalVar("v").asArray())  {
            sb.append(v.toString());
            }
        return new StringValue(sb.toString());
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Value checkReturn() {
        return new StringValue("");
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("v", Array.NAME);
        }};
    }
}
