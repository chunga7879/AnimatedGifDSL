package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Null;
import core.values.StringValue;

import java.util.HashMap;
import java.util.Map;

public class Print extends AbstractFunction {
    public final static String ACTUAL_NAME = "Print";

    @Override
    public Null call(Scope scope) {
        System.out.println("Built-in Print: " + scope.getLocalVar(AbstractFunction.PARAM_TARGET).asString().get());
        return Null.NULL;
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, StringValue.NAME);
        }};
    }

    @Override
    public Null checkReturn() {
        return Null.NULL;
    }
}
