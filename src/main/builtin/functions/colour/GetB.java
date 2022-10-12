package builtin.functions.colour;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class GetB extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-B";

    @Override
    public IntegerValue call(Scope scope) {
        Colour colour = scope.getVar(AbstractFunction.PARAM_TARGET).asColour();
        return new IntegerValue(colour.getB());
    }

    @Override
    public IntegerValue checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return new IntegerValue(0);
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Colour.NAME);
        }};
    }

    @Override
    public IntegerValue checkReturn() {
        return new IntegerValue(0);
    }
}
