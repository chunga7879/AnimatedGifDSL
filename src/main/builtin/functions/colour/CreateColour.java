package builtin.functions.colour;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class CreateColour extends AbstractFunction {
    public final static String ACTUAL_NAME = "Create-Colour";

    @Override
    public Colour call(Scope scope) {
        IntegerValue r = scope.getVar("r").asInteger();
        IntegerValue g = scope.getVar("g").asInteger();
        IntegerValue b = scope.getVar("b").asInteger();

        return new Colour(r.get(), g.get(), b.get());
    }

    @Override
    public Colour checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
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
