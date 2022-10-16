package builtin.functions.colour;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class CreateColour extends AbstractFunction {
    public final static String ACTUAL_NAME = "Create-Colour";

    @Override
    public Colour call(Scope scope) {
        IntegerValue r = scope.getLocalVar("r").asInteger();
        IntegerValue g = scope.getLocalVar("g").asInteger();
        IntegerValue b = scope.getLocalVar("b").asInteger();

        return new Colour(r.get(), g.get(), b.get());
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("r", IntegerValue.NAME);
            put("g", IntegerValue.NAME);
            put("b", IntegerValue.NAME);
        }};
    }

    @Override
    public Colour checkReturn() {
        return new Colour(0, 0, 0);
    }
}
